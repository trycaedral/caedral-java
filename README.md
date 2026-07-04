# Caedral Java SDK

Official Java client for the [Caedral API](https://caedral.com). OpenAI-compatible request shapes with idiomatic Java patterns (builder, checked streaming via `Iterator`, Jackson POJOs).

> **Coordinates:** `com.caedral:caedral-java:0.1.0-SNAPSHOT` is a placeholder. Replace with your published Maven coordinates before release.

## Installation

```xml
<dependency>
  <groupId>com.caedral</groupId>
  <artifactId>caedral-java</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Local build:

```bash
cd sdk-java
mvn package
```

## Quickstart

```java
import com.caedral.sdk.Caedral;
import com.caedral.sdk.model.ChatCompletionRequest;
import com.caedral.sdk.model.ChatMessage;
import java.util.List;

Caedral caedral = Caedral.builder()
    .apiKey("cd_live_...")
    .baseUrl("http://localhost:5001")
    .build();

var completion = caedral.chat().completions().create(
    new ChatCompletionRequest(
        "caedral-titan",
        List.of(new ChatMessage("user", "Hello!"))
    )
);

System.out.println(completion.getChoices().get(0).getMessage().get("content"));
```

Run the included example:

```bash
cd sdk-java
CAEDRAL_API_KEY=cd_live_... CAEDRAL_BASE_URL=http://localhost:5001 \
  mvn -q exec:java -Dexec.mainClass="com.caedral.sdk.examples.Quickstart"
```

## Configuration

| Builder method | Default | Description |
|----------------|---------|-------------|
| `apiKey(String)` | required | API key (`cd_live_...`) |
| `baseUrl(String)` | `https://api.caedral.com` | Gateway base URL |
| `timeout(Duration)` | 120s | HTTP timeout |
| `maxRetries(int)` | 3 | Retries for idempotent GET |
| `httpClient(OkHttpClient)` | built-in | Custom OkHttp client |
| `objectMapper(ObjectMapper)` | built-in | Custom Jackson mapper |

## Methods

### Chat completions

```java
ChatCompletion completion = caedral.chat().completions().create(request);
```

### Streaming

This SDK uses a **blocking `Iterator<ChatCompletionChunk>`** (`ChatCompletionStream`) — the standard pattern for synchronous Java clients without adding Project Reactor/RxJava. An optional **callback** API is also available.

**Iterator style (recommended):**

```java
try (ChatCompletionStream stream = caedral.chat().completions().createStream(request)) {
    while (stream.hasNext()) {
        var chunk = stream.next();
        // handle chunk
    }
}
```

**Callback style:**

```java
caedral.chat().completions().createStream(request, chunk -> {
    System.out.print(chunk.getChoices().get(0).getDelta().get("content"));
});
```

### Models

```java
ModelListResponse models = caedral.models().list();
```

### Usage

```java
UsageSummary usage = caedral.usage().get();
System.out.println(usage.getWeeklyPool().getRemaining());
```

### Embeddings

```java
caedral.embeddings().create(new EmbeddingCreateRequest("caedral-embed", "Hello world"));
```

### Images

```java
caedral.images().generate(new ImageGenerateRequest("A minimal logo on dark background"));
```

### Audio

```java
AudioGenerateRequest req = new AudioGenerateRequest("Welcome to Caedral.");
req.setVoice("alloy");
caedral.audio().generate(req);
```

### Rerank

```java
RerankCreateRequest req = new RerankCreateRequest(
    "billing",
    List.of("Caedral pricing", "Gateway port 5001")
);
req.setTopN(2);
caedral.rerank().create(req);
```

## Error handling

```java
try {
    caedral.chat().completions().create(request);
} catch (CaedralAPIException ex) {
    System.err.printf("status=%d type=%s msg=%s%n",
        ex.getStatusCode(), ex.getType(), ex.getMessage());
}
```

`CaedralNetworkException` is thrown on timeouts and connectivity failures.

## Integration tests

Requires a running local gateway on port **5001** and `DATABASE_URL` in the repo root `.env` (tests create a temporary API key automatically).

```bash
cd sdk-java
mvn test
```

Optional environment variables:

| Variable | Description |
|----------|-------------|
| `CAEDRAL_BASE_URL` | Gateway URL (default `http://localhost:5001`) |
| `CAEDRAL_TEST_API_KEY` | Use an existing key instead of creating one |

## License

MIT
