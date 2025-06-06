package my.artemyulyanov.common

public interface TimestampAwareEntity {
    public val createdAt: Timestamp
    public val updatedAt: Timestamp
}