package com.foodtruck;

import java.time.DayOfWeek;
import java.util.*;

public class FoodTruckInfoOperation implements IFoodTruckInfoOperation {
    //Day map to Map<Hour, List of FoodTruck>
    private Map<DayOfWeek, Map<Integer, List<FoodTruckInfo>>> foodTruckMapByDayAndHour;
    public FoodTruckInfoOperation() {
        foodTruckMapByDayAndHour = new HashMap();
    }

    @Override
    public List<FoodTruckInfo> getFoodTruckByCurrentDate() {
        //Get current day of week and hour of day
        Date date = new java.util.Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        //Fast retrieve the list of restaurants back based on above
        return foodTruckMapByDayAndHour.get(DayOfWeek.of(dayOfWeek)).get(hourOfDay);
    }

    @Override
    public void processData(List<String> list) {
        //Save all food truck info into foodTruckMapByDayAndHour
        for(String line : list) {
            FoodTruckInfo foodTruckInfo = new FoodTruckInfo(line);
            for(DayOfWeek dow : foodTruckInfo.getOpenDaysAndHours().keySet()) {
                foodTruckMapByDayAndHour.putIfAbsent(dow, new HashMap<>());
                for(int hour : foodTruckInfo.getOpenDaysAndHours().get(dow)) {
                    foodTruckMapByDayAndHour.get(dow).putIfAbsent(hour, new ArrayList<>());
                    foodTruckMapByDayAndHour.get(dow).get(hour).add(foodTruckInfo);
                }
            }
        }
        for(DayOfWeek dow : DayOfWeek.values()) {
            if(foodTruckMapByDayAndHour.containsKey(dow)) {
                Map<Integer, List<FoodTruckInfo>> map = foodTruckMapByDayAndHour.get(dow);
                for(int hour = 0; hour <= 23; hour++) {
                    if(map.containsKey(hour)) {
                        Collections.sort(map.get(hour), (a,b)->(a.getName().compareTo(b.getName())));
                    }
                }
            }
        }
    }
}
