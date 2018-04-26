package fontys.messaging.core.messaging.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MessageSerializer<T> {

    private final ObjectMapper objectMapper;
    private final Class<T> classToken;

    public MessageSerializer(Class<T> classToken){
        this(classToken, new ObjectMapper());
    }

    public MessageSerializer(Class<T> classToken, ObjectMapper objectMapper){
        this.classToken = classToken;
        this.objectMapper = objectMapper;
    }

    public String toString(T object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public T toObject(String string){
        try {
            return objectMapper.readValue(string, classToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
