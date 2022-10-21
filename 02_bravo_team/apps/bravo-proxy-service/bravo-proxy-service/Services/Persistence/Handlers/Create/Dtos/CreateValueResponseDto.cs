using bravo_proxy_service.Services.Persistence.Data;
using Newtonsoft.Json;

namespace bravo_proxy_service.Services.Persistence.Handlers.Create.Dtos;

public class CreateValueResponseDto
{
    [JsonProperty("value")]
    public ValueEntity Value { get; set; }
}
