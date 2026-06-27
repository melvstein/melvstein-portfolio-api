package dev.melvstein.portfolio.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
@EnableCaching
public class MelvsteinPortfolioApiApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MelvsteinPortfolioApiApplication.class, args);

        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 4, 2, 5, 1);
        int freq = mostFrequentNumber(numbers);
        // System.out.println("Freq: " + freq);

        String word = "accenture";
        int vowelsCount = countVowels(word);
        // System.out.println("vowelsCount: " + vowelsCount);

        List<Integer> duplicateNumbers = findDuplicates(numbers);
        // System.out.println("Number of duplicates: " + duplicateNumbers);

        List<String> result = getMovieTitles("spiderman");

        /*for (String title : result) {
            System.out.println(title);
        }*/
	}

    public static List<String> getMovieTitles(String substr) throws Exception {
        List<String> titles = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        int page = 1;
        int totalPages = 1;

        while (page <= totalPages) {
            String url = "https://jsonmock.hackerrank.com/api/movies/search/?Title="
                + substr + "&page=" + page;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());

            totalPages = root.get("total_pages").asInt();

            JsonNode data = root.get("data");

            for (JsonNode movie : data) {
                titles.add(movie.get("Title").asText());
            }

            page++;
        }

        Collections.sort(titles);

        return titles;
    }

    public static List<Integer> findDuplicates(List<Integer> numbers) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();

        for (Integer number : numbers) {
            if (!seen.add(number)) {
                duplicates.add(number);
            }
        }

        return new ArrayList<>(duplicates);
    }

    public static int countVowels(String word) {
        int count = 0;
        List<Character> vowels = Arrays.asList('a', 'e', 'i', 'o', 'u');

        for (char c : word.toLowerCase().toCharArray()) {
            if (vowels.contains(c)) {
                count++;
            }
        }

        return count;
    }

    public static int mostFrequentNumber(List<Integer> numbers) {
        Map<Integer, Integer> freq = new HashMap<>();

        for (Integer number : numbers) {
            freq.put(number, freq.getOrDefault(number, 0) + 1);
        }

        int maxCount = 0;
        int result = 0;

        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                result = entry.getKey();
            }
        }

        return result;
    }

    public static void tutorial() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // ArrayList - is a dynamic array that grows automatically. It allows duplicate elements and maintains the order of insertion. It is not synchronized.
        List<String> cars = new ArrayList<>();
        cars.add("BMW");
        cars.add("Volvo");
        //cars.remove(1);
        cars.addFirst("Ford");

        //Collections.shuffle(cars);

        Iterator<String> iterator = cars.iterator();

        while (iterator.hasNext()) {
            String car = iterator.next();
            System.out.println(car);
        }

        System.out.println(cars);

        // LinkedList - is a list made of connected nodes. Allows duplicates
        List<Number> nums = new LinkedList<>();
        nums.add(1);
        nums.add(2);
        nums.add(2);

        nums.addFirst(0);
        System.out.println(nums);

        // HashSet - Stores unique values using hashing. Not allows duplication. Order unpredictable
        Set<Number> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(2);
        set.add(4);

        List<Number> set1 = new ArrayList<>(set);

        System.out.println(set1);

        // LinkedHashSet - Stores unique values using hashing. Not allows duplication. Maintains insertion order

        // TreeSet - Set that automatically sorts values. Not allows duplication. Automatically sorted
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("BMW");
        treeSet.add("Volvo");
        treeSet.add("Volvo2");
        treeSet.add("Volvo3");
        treeSet.add("Volvo1");

        List<String> treeSet1 = new ArrayList<>(treeSet);

        System.out.println(treeSet1.get(0));

        Set<Number> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(5);
        linkedHashSet.add(2);
        linkedHashSet.add(3);
        linkedHashSet.add(1);

        System.out.println(linkedHashSet);

        // HashMap - Stores key-value pairs using hashing. Key not allows duplication, but values allows duplication. Order unpredictable
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("key1", "value1");
        hashMap.put("key2", 2);
        hashMap.put("key3", 2);

        String json = objectMapper.writeValueAsString(hashMap);

        System.out.println(json);

        // LinkedHashMap - HashMap that maintains insertion order. Key not allows duplication, but values allows duplication.

        // TreeMap - Map that automatically sorts keys
        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("key3", "value1");
        treeMap.put("key2", "value2");
        treeMap.put("key1", "value3");
        treeMap.put("key4", "value3");

        System.out.println(treeMap);

        Map<String, String> treeMap1 = new LinkedHashMap<>();
        treeMap1.put("key3", "value1");
        treeMap1.put("key2", "value2");
        treeMap1.put("key1", "value3");
        treeMap1.put("key4", "value3");

        System.out.println(treeMap1);
    }

}
