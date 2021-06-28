package il.co.rootsapp

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalDateTimeAdapter : JsonDeserializer<LocalDateTime>,
    JsonSerializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): LocalDateTime {
        return LocalDateTime.parse(
            json.asJsonPrimitive.asString,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        )
    }

    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}
