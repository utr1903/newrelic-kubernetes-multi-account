package list

type ListResponseDto struct {
	Value *[]Value `json:"values"`
}

type Value struct {
	Id    string `json:"id"`
	Value string `json:"value"`
	Tag   string `json:"tag"`
}
