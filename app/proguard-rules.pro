# Con R8 hay que conservar los DTO: si se renombran los campos, Gson no puede leer el JSON.
-keep class com.upb.pokeapi.data.remote.** { *; }

# Gson / Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
