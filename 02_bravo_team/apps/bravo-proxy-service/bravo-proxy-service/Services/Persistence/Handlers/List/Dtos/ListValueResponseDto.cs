using bravo_proxy_service.Services.Persistence.Data;
using Newtonsoft.Json;

namespace bravo_proxy_service.Services.Persistence.Handlers.List.Dtos;

public class ListValueResponseDto
{
    [JsonProperty("values")]
    public List<ValueEntity> Values { get; set; }
}
