package create

type CreateResponseDto struct {
	Value *Value `json:"value"`
}

type Value struct {
	Id    string `json:"id"`
	Value string `json:"value"`
	Tag   string `json:"tag"`
}
