package com.yikolemon.service;

import com.yikolemon.mapper.DataMapper;
import com.yikolemon.pojo.DAUData;
import com.yikolemon.pojo.UVData;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DataServiceImpl implements DataService{

    // Constructor: Schedule cleanup task, sync task, and load data on startup
    public DataServiceImpl() {
        cleanupScheduler.scheduleAtFixedRate(this::cleanupExpiredData, 1, 1, TimeUnit.DAYS); // Clean expired data
        syncScheduler.scheduleAtFixedRate(this::syncDataToDatabase, 1, 1, TimeUnit.HOURS); // Sync UV/DAU to DB every hour
    }

    @PostConstruct
    public void init() {
        loadDataFromDatabase(); // Load data from database on startup
    }

    private final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    // In-memory data structures to store UV and DAU data
    private final Map<String, Set<String>> uvData = new ConcurrentHashMap<>();
    private final Map<String, BitSet> dauData = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupScheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService syncScheduler = Executors.newScheduledThreadPool(1);

    // Inject DataMapper for database persistence
    @Resource
    private DataMapper dataMapper;

    // Record unique visitor (UV) for a specific IP
    public void recordUV(String ip) {
        String dateKey = df.format(new Date());
        uvData.computeIfAbsent(dateKey, k -> Collections.synchronizedSet(new HashSet<>())).add(ip);
    }

    // Record daily active user (DAU) for a specific user ID
    public void recordDAU(int userId) {
        String dateKey = df.format(new Date());
        dauData.computeIfAbsent(dateKey, k -> new BitSet()).set(userId);
    }

    // Sync data from memory to database
    public void syncDataToDatabase() {
        // Sync UV data
        for (Map.Entry<String, Set<String>> entry : uvData.entrySet()) {
            String dateKey = entry.getKey();
            Set<String> ips = entry.getValue();
            for (String ip : ips) {
                dataMapper.insertUVData(dateKey, ip);
            }
        }

        // Sync DAU data
        for (Map.Entry<String, BitSet> entry : dauData.entrySet()) {
            String dateKey = entry.getKey();
            BitSet bitSet = entry.getValue();
            for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
                dataMapper.insertDAUData(dateKey, i);
            }
        }

        System.out.println("Data synchronized to database.");
    }

    // Load data from the database to memory on startup
    public void loadDataFromDatabase() {
        // Load UV data from the database
        List<UVData> uvDataList = dataMapper.getAllUVData();
        for (UVData uvDataRecord : uvDataList) {
            String dateKey = uvDataRecord.getDateKey();
            String ip = uvDataRecord.getIp();
            uvData.computeIfAbsent(dateKey, k -> Collections.synchronizedSet(new HashSet<>())).add(ip);
        }

        // Load DAU data from the database
        List<DAUData> dauDataList = dataMapper.getAllDAUData();
        for (DAUData dauDataRecord : dauDataList) {
            String dateKey = dauDataRecord.getDateKey();
            int userId = dauDataRecord.getUserId();
            dauData.computeIfAbsent(dateKey, k -> new BitSet()).set(userId);
        }

        System.out.println("Data loaded from database into memory.");
    }

    // Calculate UV for the last seven days
    public long lastSevenDayUv() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        Date end = calendar.getTime();
        return calculateUv(start, end);
    }

    // Calculate DAU for the last seven days
    public long lastSevenDayDau() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        Date end = calendar.getTime();
        return calculateDAU(start, end);
    }

    // Calculate UV for a specified date range
    public long calculateUv(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Set<String> uniqueIPs = new HashSet<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String dateKey = df.format(calendar.getTime());
            Set<String> ips = uvData.getOrDefault(dateKey, Collections.emptySet());
            uniqueIPs.addAll(ips);
            calendar.add(Calendar.DATE, 1);
        }
        return uniqueIPs.size();
    }

    // Calculate DAU for a specified date range
    public long calculateDAU(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        BitSet resultBitSet = new BitSet();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String dateKey = df.format(calendar.getTime());
            BitSet dailyBitSet = dauData.getOrDefault(dateKey, new BitSet());
            resultBitSet.or(dailyBitSet);
            calendar.add(Calendar.DATE, 1);
        }
        return resultBitSet.cardinality();
    }

    // Cleanup expired data (keep data for the last 8 days)
    public void cleanupExpiredData() {
        String expiryDateKey = df.format(getDateBeforeDays(8));
        uvData.keySet().removeIf(dateKey -> dateKey.compareTo(expiryDateKey) < 0);
        dauData.keySet().removeIf(dateKey -> dateKey.compareTo(expiryDateKey) < 0);

        // Clean up expired data in the database
        dataMapper.deleteExpiredUVData(expiryDateKey);
        dataMapper.deleteExpiredDAUData(expiryDateKey);
    }

    // Helper method to get a date before a certain number of days
    private Date getDateBeforeDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    // Shutdown cleanup scheduler
    public void shutdown() {
        cleanupScheduler.shutdown();
        syncScheduler.shutdown();
    }

    @Override
    public List<UVData> getAllUVDataFromDb() {
        return dataMapper.getAllUVData();
    }

    @Override
    public List<DAUData> getAllDAUDataFromDb() {
        return dataMapper.getAllDAUData();
    }

    // Example usage
    public static void main(String[] args) throws InterruptedException {
        DataServiceImpl tracker = new DataServiceImpl();

        // Record some UVs and DAUs
        tracker.recordUV("192.168.0.1");
        tracker.recordUV("192.168.0.2");
        tracker.recordDAU(1);
        tracker.recordDAU(2);

        // Wait for a day to simulate aging data
        Thread.sleep(2000); // Replace with realistic timing in production
        tracker.recordUV("192.168.0.3");
        tracker.recordDAU(3);

        // Calculate UV and DAU
        System.out.println("Last 7 days UV: " + tracker.lastSevenDayUv());
        System.out.println("Last 7 days DAU: " + tracker.lastSevenDayDau());

        // Shutdown scheduler after some time
        Thread.sleep(5000); // Simulating a longer runtime
        tracker.shutdown();
    }
}

