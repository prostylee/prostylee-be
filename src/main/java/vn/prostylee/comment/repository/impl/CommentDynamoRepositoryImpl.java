package vn.prostylee.comment.repository.impl;

import com.hazelcast.map.impl.query.QueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import vn.prostylee.comment.config.AwsAppSyncProperties;
import vn.prostylee.comment.config.AwsDynamoDbProperties;
import vn.prostylee.comment.entity.CommentTable;
import vn.prostylee.comment.repository.CommentDynamoRepository;
import vn.prostylee.core.constant.TargetType;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class CommentDynamoRepositoryImpl implements CommentDynamoRepository {

    public static final String COMMENT_TABLE_NAME = "Comment";
    private final AwsDynamoDbProperties awsDynamoDbProperties;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient ;
    private final DynamoDbClient dynamoDbClient;

    @Override
    public Long count(Long targetId, TargetType targetType) {
        // TODO Create secondary index for scanning faster
//        Condition targetIdCondition = Condition.builder()
//                .comparisonOperator(ComparisonOperator.EQ)
//                .attributeValueList(AttributeValue.builder().s(String.valueOf(targetId)).build())
//                .build();
//
//        Condition targetTypeCondition = Condition.builder()
//                .comparisonOperator(ComparisonOperator.EQ)
//                .attributeValueList(AttributeValue.builder().s(targetType.name()).build())
//                .build();
//
//        Map<String, Condition> keyConditions = new HashMap<>();
//        keyConditions.put("targetId", targetIdCondition);
//        keyConditions.put("targetType", targetTypeCondition);
//
//        QueryRequest request = QueryRequest.builder()
//                .tableName("Comment-2ac6njmmizg5baippa5gfm4a6e-staging")
//                .select(Select.COUNT)
//                .keyConditions(keyConditions)
//                .build();
//
//        QueryResponse response = dynamoDbClient.query(request);
//        return Long.valueOf(response.count());

        DynamoDbTable<CommentTable> orderTable = getTable();

        Map<String, String> expressionNames = new HashMap<>();
        expressionNames.put("#targetId", "targetId");
        expressionNames.put("#targetType", "targetType");

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":targetId", AttributeValue.builder().s(String.valueOf(targetId)).build());
        expressionValues.put(":targetType", AttributeValue.builder().s(targetType.name()).build());

        Expression expression = Expression.builder()
                .expressionNames(expressionNames)
                .expressionValues(expressionValues)
                .expression("#targetId = :targetId and #targetType = :targetType")
                .build();

        PageIterable<CommentTable> results = orderTable.scan(r -> r.filterExpression(expression));
        return results.items().stream().count();
    }

    private DynamoDbTable<CommentTable> getTable() {
        return dynamoDbEnhancedClient.table(COMMENT_TABLE_NAME + awsDynamoDbProperties.getTableNameSuffix(), TableSchema.fromBean(CommentTable.class));
    }
}
