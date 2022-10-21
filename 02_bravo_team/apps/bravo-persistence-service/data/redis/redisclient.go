package redis

import (
	"bravo-persistence-service/commons"
	"bravo-persistence-service/entities"
	"encoding/json"
	"errors"

	"github.com/go-redis/redis"
	"github.com/rs/zerolog"
)

type RedisClient struct {
	RedisClient *redis.Client
}

func CreateRedisInstance() *RedisClient {

	// Connect to Redis
	commons.Log(zerolog.InfoLevel, "Connecting to Redis...")

	client := redis.NewClient(&redis.Options{
		Addr:     "redis.bravo.svc.cluster.local:6379",
		Password: "",
		DB:       0,
	})

	// Ping Redis
	pong, err := client.Ping().Result()
	commons.Log(zerolog.InfoLevel, "Ping?"+pong)

	// Panic if ping fails
	if err != nil {
		message := "Connecting to Redis is failed."
		commons.Log(zerolog.PanicLevel, err.Error())
		commons.Log(zerolog.PanicLevel, message)
		panic(message)
	}

	// Create the Redis instance
	var redis *RedisClient = &RedisClient{
		RedisClient: client,
	}

	commons.Log(zerolog.InfoLevel, "Connected to Redis successfully.")

	return redis
}

func (redisClient RedisClient) Insert(
	entity *entities.Entity,
) (
	err error,
) {

	// Serialize the entity
	entityAsBytes, err := json.Marshal(entity)
	if err != nil {
		commons.Log(zerolog.ErrorLevel, "Connecting to Redis is failed.")
		return errors.New("connecting to redis is failed")
	}

	// Set entity to cache
	err = redisClient.RedisClient.Set(entity.Id, entityAsBytes, 0).Err()
	if err != nil {
		commons.Log(zerolog.ErrorLevel, "Inserting entity with ID"+entity.Id+"is failed.")
		return errors.New("inserting entity with id" + entity.Id + "is failed")
	}

	return nil
}

func (redisClient RedisClient) Get(
	id string,
) *entities.Entity {

	// Get entity from cache
	entityAsString, err := redisClient.RedisClient.Get(id).Bytes()
	if err != nil {
		commons.Log(zerolog.InfoLevel, "Entity with ID"+id+"does not exist in the cache.")
	}

	var entity *entities.Entity
	json.Unmarshal(entityAsString, entity)

	return entity
}
