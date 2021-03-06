# Item Service
This serivce is microservice, provide ability to post Item i.e, event(id, timestamp). This Service does not have any external data store, uses memory cache. This service is designed to return items posted in last Nth seconds or last N item posted, which ever is greater.

# Item Cache Information
Given the requirement for high throughput and accessibility of item in last n seconds or last n posted item, I considered using ConcurrentSkipListMap due to support for concurrency and ordered access . The map is sorted in natural order of key or by comparator passed which addresses usecase for ordered access.  The average complexity of this map is O(log n) for insertion and search, provides concurrency and implement ConcurrentNavigableMap which adds extensive ability to work with map for given usecases. 

Initially I thought of using two datastructure ConcurrentHashMap having O(1) for insert and search and LinkList O(n) for search, to handle given usecases however ConcurrentSkipList data-structure provided similar to more functionality at avg O(log n) complexity with concurrency. This service uses ConcurrentSkipList datastructure for cache implementation. However for evaluation of other option I wrote standalone code test\java\com\sap\task\items\cache\ConcurrentLinkedMap.java in test package.

Apart from datastructure, handling cache eviction for better memory foot print is essential. Thought of multiple approach to handle eviction ie., expireAfterInterval, expireLRU or expireAfterAccess or peform capacity check at put etc. I considered CacheManager which spawn scheduled thread to evaluate high memory usage and then start eviction of configured percentage of total elements from cache in FIFO order such that oldest element will be removed first. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Installing
```
git clone https://github.com/thismohsin/sap-task-items-app.git
```
##  Running the build and tests
- Demo-able unit test to verify functionality, specifically the two unit-test listed below.
- Coverage Details: Class (95%), Method(90%), Line (92%) 
####  Unit Test:
```
com\sap\task\items\cache\CacheTest.java
com\sap\task\items\service\ItemServiceTest.java
```
####  Integration Test : 
```
com\sap\task\items\it\ItemServiceIntegrationTest.java
```
#### Build Artifact : 
```
gradlew clean build
```
#### Deployment
```
java -jar build\libs\sap-task-items-app-1.0.0.jar
```

#### REST API
POST /items <br/> Content-Type: application/json <br/> Response code: 201 Created
```
{
        "item": {
            "id": 1,
            "timestamp": "2016-01-01T23:01:01.001Z" 
        }
}
```
GET /items <br/> Response Code: 200 OK
```
[
    {
        "item": {
            "id": 1,
            "timestamp": "2016-01-01T23:01:01.001Z"
        }
    },
    {
        "item": {
            "id": 2,
            "timestamp": "2016-01-01T23:01:01.002Z"
        }
    }
]
```

## Built With
* Java
* SpringFramework
* Junit and Mockito
* Gradle

