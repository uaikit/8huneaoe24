package org.example

import java.io.File
import java.util.Base64

fun main() {
    val apiKey = System.getenv("GEMINI_API_KEY")
    val gemini = Gemini(apiKey)
    val text = "Write a story about a magic backpack."
    val inputJson =
        GenerateContentRequest(
            listOf(Content(listOf(Part(text)))),
            safetySettings =
                listOf(
                    SafetySetting(
                        category = HarmCategory.HARM_CATEGORY_HARASSMENT,
                        threshold = Threshold.BLOCK_ONLY_HIGH,
                    ),
                ),
        )
    println(gemini.generateContent(inputJson, model = "gemini-1.0-pro").candidates[0].content.parts[0].text!!.replace("\n\n", "\n"))
    val inputJson2 = CountTokensRequest(listOf(Content(listOf(Part(text)))))
    println(gemini.countTokens(inputJson2))
    val embedRequest =
        EmbedContentRequest(
            content = Content(listOf(Part(text))),
            model = "models/embedding-001",
        )
    println(gemini.embedContent(embedRequest, model = "embedding-001"))
    val batchEmbedRequest =
        BatchEmbedRequest(
            listOf(
                EmbedContentRequest(
                    content = Content(listOf(Part(text))),
                    model = "models/embedding-001",
                ),
            ),
        )
    println(gemini.batchEmbedContents(batchEmbedRequest, model = "embedding-001"))

    println(gemini.getModels())

    val path = Any::class.java.getResource("/scones.jpg")
    val imagePath = "scones.jpg"
    val imageFile = File(imagePath)
    val image = File(path.toURI())
    val base64Image = Base64.getEncoder().encodeToString(image.readBytes())

    val inputWithImage =
        GenerateContentRequest(
            listOf(
                Content(
                    listOf(
                        Part(text = "What is this picture?"),
                        Part(
                            inlineData =
                                InlineData(
                                    mimeType = "image/jpeg",
                                    data = base64Image,
                                ),
                        ),
                    ),
                ),
            ),
        )
    println(gemini.generateContent(inputWithImage, "gemini-pro-vision").candidates[0].content.parts[0].text!!.replace("\n\n", "\n"))
}

class ITTest
