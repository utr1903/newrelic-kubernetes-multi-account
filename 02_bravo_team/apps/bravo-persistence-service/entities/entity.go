package entities

type Entity struct {
	Id    string `bson:"_id"`
	Value string `bson:"value"`
	Tag   string `bson:"tag"`
}
