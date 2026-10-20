# kotlinx.serialization: giữ serializer của các model được lưu xuống DataStore
-keepattributes *Annotation*, InnerClasses
-keepclassmembers @kotlinx.serialization.Serializable class com.lifequest.app.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}
