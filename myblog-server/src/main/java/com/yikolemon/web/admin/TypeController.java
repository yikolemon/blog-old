package com.yikolemon.web.admin;

import com.yikolemon.pojo.Type;
import com.yikolemon.service.TypeService;
import com.yikolemon.service.TypeServiceImpl;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RequiresRoles("admin")
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(Model model){
        List<Type> allTypes = typeService.getAllTypes();
        model.addAttribute("types",allTypes);
        return "admin/types";
    }

    @PostMapping("/types")
    public String saveType(@Valid Type type, BindingResult result, RedirectAttributes redirectAttributes){
        if (typeService.getTypeByName(type.getName())!=null){
            result.rejectValue("name","nameError","已存在该类型");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }
        //如果id为空的话说明是新增分类，如果不为空的话说明是修改更新分类
        if (type.getId()!=null) {
            int j = typeService.updateType(type);
            if (j==1){
                redirectAttributes.addFlashAttribute("message","更新成功");
            }
            else {
                redirectAttributes.addFlashAttribute("message","更新失败");
            }
            return "redirect:../admin/types";
        }
        int i = typeService.saveType(type);
        if (i==1){
            redirectAttributes.addFlashAttribute("message","操作成功");
        }
        else {
            redirectAttributes.addFlashAttribute("message","操作失败");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        int i = typeService.deleteType(id);
        if (i==1){
            redirectAttributes.addFlashAttribute("message","删除成功");
        }
        else {
            redirectAttributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/types";
    }

}
