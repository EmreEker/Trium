
# Trium
Trium is a multi-Maven project consisting of two Spring Boot modules:

## TriumCore: Contains the Trie structure.
## TriumBoot: Developed to be added as a dependency to Spring Boot projects.
### TriumCore
TriumCore contains the fundamental interfaces and classes of the trie data structure. These interfaces and classes facilitate the creation, configuration, and usage of the trie structure.

The trie structure is a data structure that enables fast and efficient searching. The basic idea of the trie structure is to represent a search term in a tree-like structure. Each search term corresponds to a node in the tree structure. Nodes are composed of characters from the search term.

The trie structure allows searching based on the prefix of the search term. For example, to find all matches for the word "John," all nodes starting with the letter "J" are searched in the trie structure.

The trie structure is utilized in various applications. For instance, it is employed in autocomplete, spell checking, and word suggestion systems

### TriumBoot
TriumBoot consists of the ResponseMessageConverter class. This class provides serialization and deserialization of Response objects in JSON format in Spring Boot projects.

## Usage
To use the Trium modules, you can follow these steps:

Add the Trium project to Maven:
```
<dependency>
    <groupId>com.trium</groupId>
    <artifactId>Trium</artifactId>
    <version>1.0.0</version>
</dependency>
```
Create the BasicAutoComplete bean from the TriumCore module:

```java
@Bean
public BasicAutoComplete getTrie() {
    return new BasicAutoComplete(Configure.initFromFile("names.txt", ","));
}
```
Create the ResponseMessageConverter bean from the TriumBoot module:
```
@Bean
public ResponseMessageConverter responseMessageConverter() {
    return new ResponseMessageConverter();
}
```
You can use the BasicAutoComplete bean to use the Trie structure:
```java
@Autowired
BasicAutoComplete basicAutoComplete;

@GetMapping(value = "/ace")
public Response autoSearchEngine(@RequestParam(name = "searchTerm") String searchTerm) {
    return basicAutoComplete.find(searchTerm);
}
```
Example
The following code creates a simple Spring Boot application using the Trium modules:

```java
@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

}

@Configuration
class Configs {

    @Bean
    public ResponseMessageConverter responseMessageConverter() {
        return new ResponseMessageConverter();
    }

    @Bean
    public BasicAutoComplete getTrie() {
        return new BasicAutoComplete(Configure.initFromFile("names.txt", ","));
    }
}

@RestController
class Controller {

    @Autowired
    BasicAutoComplete basicAutoComplete;

    @GetMapping(value = "/ace")
    public Response autoSearchEngine(@RequestParam(name = "searchTerm") String searchTerm) {
        return basicAutoComplete.find(searchTerm);
    }
}
```

When you run this application, you can search the Trie structure by sending a GET request to the /ace endpoint. For example, you can get all matches for the word "John" by sending a GET request to http://localhost:8080/ace?searchTerm=John.

## Conclusion
Trium is a project that makes it easier to use the Trie structure in Spring Boot applications. Using the Trium modules, you can perform fast and efficient search in your applications.

I hope this is what you were looking for. Let me know if you have any other questions.

## License

[MIT](https://gitlab.com/emrexps/trium/-/raw/main/LICENCE.txt?ref_type=heads)