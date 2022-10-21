using Newtonsoft.Json;

namespace bravo_proxy_service.Services.Persistence.Handlers.Create.Dtos;

public class CreateValueRequestDto
{
    [JsonProperty("value")]
    public string? Value { get; set; }

    [JsonProperty("tag")]
    public string? Tag { get; set; }
}
