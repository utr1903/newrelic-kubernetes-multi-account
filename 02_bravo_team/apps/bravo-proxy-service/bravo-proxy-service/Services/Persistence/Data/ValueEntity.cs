using Newtonsoft.Json;

namespace bravo_proxy_service.Services.Persistence.Data;

public class ValueEntity
{
    [JsonProperty("id")]
    public string Id { get; set; }

    [JsonProperty("value")]
    public string Value { get; set; }

    [JsonProperty("tag")]
    public string Tag { get; set; }
}

