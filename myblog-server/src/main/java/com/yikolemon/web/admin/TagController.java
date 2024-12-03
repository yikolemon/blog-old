package com.yikolemon.web.admin;

import com.yikolemon.pojo.Tag;
import com.yikolemon.service.TagService;
import com.yikolemon.service.TagServiceImpl;
import org.apache.shiro.authz.annotation.RequiresRoles;
import javax.annotation.Resource;
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
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(Model model){
        List<Tag> allTags = tagService.getAllTags();
        model.addAttribute("tags",allTags);
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String saveTag(@Valid Tag tag, BindingResult result, RedirectAttributes redirectAttributes){
        if (tagService.getTagByName(tag.getName())!=null){
            result.rejectValue("name","nameError","已存在该标签");
        }
        if (result.hasErrors()){
            return "admin/tags-input";
        }
        //如果id为空的话说明是新增标签，如果不为空的话说明是修改更新标签
        if (tag.getId()!=null) {
            int j = tagService.updateTag(tag);
            if (j==1){
                redirectAttributes.addFlashAttribute("message","更新成功");
            }
            else {
                redirectAttributes.addFlashAttribute("message","更新失败");
            }
            return "redirect:../admin/tags";
        }
        int i = tagService.saveTag(tag);
        if (i==1){
            redirectAttributes.addFlashAttribute("message","操作成功");
        }
        else {
            redirectAttributes.addFlashAttribute("message","操作失败");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        int i = tagService.deleteTag(id);
        if (i==1){
            redirectAttributes.addFlashAttribute("message","删除成功");
        }
        else {
            redirectAttributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/tags";
    }

}
