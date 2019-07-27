package com.github.cuinipeng.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class JacksonUsage {
    private static Logger logger = LoggerFactory.getLogger(JacksonUsage.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public void run() {
        logger.info("JacksonUsage.run()");
        try {
            String data = readFile("data.json");
            Person cuinipeng = JacksonUsage.fromJson(data);
            logger.info(cuinipeng.toString());
            logger.info(JacksonUsage.toJson(cuinipeng));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toJson(Person o) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }

    public static Person fromJson(String value) throws IOException {
        return mapper.readValue(value, Person.class);
    }

    private String readFile(String filename)
            throws IOException {
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        return new String(data);
    }
}

class Address {
    private String province;
    private String city;
    private String street;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return String.format("%s, %s %s", street, city, province);
    }
}

class Person {
    private String name;
    private String email;
    private List<String> skills;
    private Address address;
    private Map<String, String> additional;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<String, String> getAdditional() {
        return additional;
    }

    public void setAdditional(Map<String, String> additional) {
        this.additional = additional;
    }

    @Override
    public String toString() {
        return String.format("{name=%s,email=%s,skills=%s,address=%s,additional=%s",
                name, email, skills, address, additional);
    }
}