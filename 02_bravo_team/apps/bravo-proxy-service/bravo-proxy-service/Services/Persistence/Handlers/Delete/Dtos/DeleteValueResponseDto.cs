using Newtonsoft.Json;

namespace bravo_proxy_service.Services.Persistence.Handlers.Delete.Dtos
{
    public class DeleteValueResponseDto
    {
        [JsonProperty("id")]
        public string? Id { get; set; }
    }
}

