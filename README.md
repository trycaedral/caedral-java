# Caedral Java SDK

Official Java client for the [Caedral API](https://caedral.com) (**v1.0.0**).

OpenAI-compatible request shapes with a builder API, Jackson models, and streaming support.

> Not on Maven Central yet — build from this repository / the **v1.0.0** GitHub Release.

## Install from source

```bash
git clone https://github.com/trycaedral/caedral-java.git
cd caedral-java
git checkout v1.0.0
mvn clean install
```

## Quickstart

```java
import com.caedral.sdk.Caedral;
import com.caedral.sdk.model.ChatCompletionRequest;
import com.caedral.sdk.model.ChatMessage;
import java.util.List;

Caedral caedral = Caedral.builder()
    .apiKey("cd_live_...")
    .build(); // default base URL: https://api.caedral.com

var completion = caedral.chat().completions().create(
    new ChatCompletionRequest(
        "caedral-olympus",
        List.of(new ChatMessage("user", "Hello!"))
    )
);

System.out.println(completion.getChoices().get(0).getMessage().get("content"));
```

## Models

**Chat:** `caedral-base` · `caedral-titan` · `caedral-olympus` · `caedral-primordial`  
**Specialized:** `caedral-vision` · `caedral-embed` · `caedral-voice` · `caedral-rerank`

API keys use the `cd_live_` prefix. Usage bills from prepaid balance.

## Docs

- Product docs: https://caedral.com/docs/java
- API: https://api.caedral.com

## License

MIT
