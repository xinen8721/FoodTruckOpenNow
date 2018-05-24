# FoodTruckOpenNow
Find Open Foodtrucks and Address at current time based in San Francisco
# Install
1. Intellij Community Verion
https://www.jetbrains.com/idea/download/#section=mac
2. Open Intellij, and navigate to File->New -> Project From Version Control -> Git
3. clone project url: git@github.com:xinen8721/FoodTruckOpenNow.git
4. If the cloning process failed due to some permission issue, go to https://github.com/xinen8721/FoodTruckOpenNow and click "Clone or download" and select "Download ZIP" to your local machine.
5. Unzip the FoodTruckOpenNow-master.zip file
6. From Intellij, navigate to File->New->Project from existing resoures and select the FoodTruckOpenNow-master folder and then click "open"

7. Ideally, the project would be created.(I have verified from another computer)
8. Navigate to Run->Run 
9. You will be entering a interacting mode of this program. 

# Run the program
1. Type in "help" to get list of commands that are available.
2. Command "show-open-foodtrucks" that will use current time in calendar and return the first page with 10 results of all opened foodtrucks
3. Command "next-page" to display next page of 10 results of all opened foodtrucks if there are any.
4. Command "prev-page" to display previous page of 10 results of all opened foodtrucks if there are any.
5. Command "end" will exit out the program.

# Test the program
1. By default, it is using currrent day of week and hour of the day to determine the list of foodtrucks that are open now. To test more time stamp, you can simply go to https://github.com/xinen8721/FoodTruckOpenNow/blob/master/src/com/foodtruck/FoodTruckInfoOperation.java#L19-L22 to select arbitrarily any day(From 1 to 7, which will translate to Monday to Sunday) and hour (from 0 to 23 since I use the 24 hour format). 

2. Check the output list of foodtrucks and verify from https://github.com/xinen8721/FoodTruckOpenNow/blob/master/src/resources/data.csv to check if the foodtruck is really open at the input you just simulated.

3. Current program supports outputing next page and previous page, check if every page contains max 10 items, especially the last page, for example, there are 121 total foodtrucks open now, there should be 13 pages while the last page should only have ONE item.

4. Check if all results are sorted alphabetically.

# Difference between command-based program and Webapp for FoodTruckOpenNow
1. Command-based propram will only serve individual user, it doesn't consider the scaling bottleneck. The webapp will need to consider that for sure, in my opinion, this service will be READ-HEAVY traffic, which means that users read more than write. So reducing the reading operation lattency matters, for all the foodtruck info, we can store all of them in NoSQL database, like DynamoDB(AWS), and set up a cache layer(Redis or Memcached) sitting in between Database and server to cache the list of foodtrucks that are open in current time, so when cache layer has the result already, return the response directly back to user which is really fast. If the request traffic goes up like crazy, simply scale out DB layer/Cache layer/Server layer to handle the huge amount of traffic to guarantee the user experience. 

2. Second difference that I can think of is abusing, for example, to prevent DOS attack or any other possible malicious behavior, in Webapp, we need to set up a throttling mechanism to prevent such things happen, the actual implementation could be using an API Rate limiter, if the request from same ip address hit the server 100 time within 1 seconds, then throttle the request.

3. Trade-off that I made, like I mentioned before, if FoodTruckOpenNow is a Webapp, then it should be READ-Heavy, so reducing the latency for every read request is really important, we can make this happen by costing more storage space, it is a lot faster compared to using less space but longer latency for each read operation since we have to process all the date at runtime. From the command-based FoodTruckOpenNow, we cannot really tell the performance difference between READ and WRITE.

