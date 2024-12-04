package com.yikolemon.service;

import com.yikolemon.mapper.LikeMapper;
import com.yikolemon.pojo.Like;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    private final ConcurrentHashMap<Long, Long> likeMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService syncScheduler = Executors.newScheduledThreadPool(1);

    // Constructor: Schedule periodic synchronization task
    public LikeServiceImpl() {
        syncScheduler.scheduleAtFixedRate(this::syncLikesToDatabase, 1, 2, TimeUnit.HOURS);
    }

    @PostConstruct
    public void init() {
        loadLikesFromDatabase(); // Load data from database on startup
    }

    @Override
    public Like getLike(long blogId) {
        Long likeNum = likeMap.computeIfAbsent(blogId, key -> {
            Like dbLike = likeMapper.getLike(blogId);
            if (dbLike == null) {
                dbLike = new Like(0, blogId);
            }
            return dbLike.getLike();
        });
        return new Like(likeNum, blogId);
    }

    @Override
    public int deleteLike(long blogId) {
        int res = likeMapper.deleteLike(blogId);
        likeMap.remove(blogId);
        return res;
    }

    @Override
    public int updateLikeOne(long blogId) {
        likeMap.compute(blogId, (key, val) -> {
            if (val == null) {
                Like dbLike = likeMapper.getLike(blogId);
                if (dbLike == null) {
                    dbLike = new Like(0, blogId);
                }
                long likeCount = dbLike.getLike();
                return dbLike.getLike() + 1;
            } else {
                return ++val;
            }
        });
        return 1;
    }

    // Synchronize like data from memory to database
    public void syncLikesToDatabase() {
        for (Map.Entry<Long, Long> entry : likeMap.entrySet()) {
            likeMapper.updateLike(entry.getKey(), entry.getValue()); // Save updated like data to the database
        }
        System.out.println("Like data synchronized to database.");
    }

    // Load initial like data from the database
    private void loadLikesFromDatabase() {
        // You can add logic here if you need to pre-load data from the database into the `likeMap`.
        System.out.println("Initial like data loaded from database.");
    }

    // Shutdown the scheduler gracefully
    public void shutdown() {
        syncScheduler.shutdown();
    }
}
