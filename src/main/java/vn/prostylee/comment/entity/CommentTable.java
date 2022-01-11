package vn.prostylee.comment.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.ZonedDateTime;

@Data
@DynamoDbBean
public class CommentTable {

    private String id;

    private String content;

    private String targetId;

    private String targetType;

    private Long numberOfLikes;

    private String parentId;

    private String ownerId;

    private String ownerFullname;

    private String owner;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

}
