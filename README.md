## Memory Cache


**Java Memory Cache, geçici bellekte veri depolamak ve sık erişilen verilere hızlı erişim sağlamak için kullanılan bir mekanizmadır. Bellekte yer alan bir önbellek, veritabanı, dosya sistemi veya diğer kaynaklardan verileri alır ve daha sonraki erişimler için hafızada tutar. Bu sayede veriler daha hızlı bir şekilde erişilebilir hale gelir ve tekrarlayan veritabanı sorguları veya ağ istekleri gereksiz yere azaltılır.**

**Java'da bellek önbelleğini uygulamak için çeşitli kütüphaneler bulunmaktadır. En yaygın kullanılanlarından biri, Java Caching System (JCS) olarak bilinen Apache Commons JCS'dir. JCS, bellek önbelleği için geniş bir işlevsellik sunar ve farklı bellek stratejileri, bellek boyutu yönetimi ve veri önbelleğe alma politikaları gibi özellikleri destekler.**

**Java bellek önbelleği için bazı temel anotasyonlar aşağıdaki gibidir:**

1. **@Cacheable: Bu anotasyon, metot çağrıldığında sonucun önbelleğe alınacağını belirtir. Önbellekte bir eşleşme varsa, metot tekrar çalıştırılmaz ve sonuç önbellekten döndürülür.**
2. **@CachePut: Bu anotasyon, metot sonucunu her zaman önbelleğe almayı sağlar. Metot her çağrıldığında sonuç önbelleğe güncellenir veya eklenir.**
3. **@CacheEvict: Bu anotasyon, belirli bir anahtara sahip bir girdiyi önbellekten silmek için kullanılır. Önbellekten veriyi kaldırmak, bir sonraki çağrıda güncel verinin tekrar alınmasını sağlar.**
4. **@Caching: Bu anotasyon, birden fazla bellek anotasyonunu aynı metoda uygulamak için kullanılır. Örneğin, hem @Cacheable hem de @CachePut anotasyonlarını aynı metota uygulamak isterseniz, @Caching anotasyonu kullanılabilir.**

**Bu anotasyonlar, Spring Framework gibi birçok Java tabanlı çerçeve tarafından desteklenir. Bu çerçeveler, bellek önbelleği konusunda daha fazla esneklik ve özelleştirme seçeneği sunar.**

![](/images/1-cache.png)


![](/images/2-cache.png)




```java
@Override
@Cacheable(value = "persons",key = "#name",condition = "#name.length() > 4", unless = "#result.age < 24" )
public Person getPersonByName(String name)  {

        try {
        logger.info("Cagirmadan once 2 saniye bekle");
        Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        return personRepository.findByName(name).orElseThrow(() -> new RuntimeException("Person bulunamadi"));
        }
```

> **Condition (Koşul):** **`condition`** ifadesi, önbelleğe alınacak verinin belirli bir koşulu **sağlaması** gerektiğini belirtir. Bu koşul, metot çağrıldığında değerlendirilir ve koşulun doğru dönmesi durumunda veri önbelleğe alınır. Koşulun yanlış dönmesi durumunda ise veri önbelleğe alınmaz ve her zaman doğrudan kaynaktan alınır.
>

> **Unless (Koşul Dışında):** **`unless`** ifadesi, önbelleğe alınacak verinin belirli bir koşulu **sağlamaması** durumunda önbelleğe alınmayacağını belirtir. **`unless`** ifadesi de **`condition`** ifadesi gibi SpEL ifadelerini kullanır.
>

**Örneğin bizim kullandığımız şu şekildedir:**

**Burada şundan bahsediyoruz koşul olarak (condition) isim uzunluğu 4’ün üzerindeyse kabul et aynı zamanda koşul sağlanmadığında (Unless) yaşın 24’den küçük yazmışız yani 24’ten büyük olması gerekmektedir. Kısacası Koşulumuz aslında şu şekildedir isim uzunluğu 4’ten büyük yaşı da 24’den büyük olmalıdır.**

***27 yaşında*** ***Burak adlı kullanıcıyı çağırdığımızda ilk başta 2.04 saniyede gelse de sonrasında cache’de olacağı için 17 ms gibi kısa bir sürede gelmiş olacaktır.***

![](/images/3-cache.png)
![](/images/4-cache.png)


**Aynı işlemleri 21 yaşındaki Burhan adlı kullanıcıyı çağırdığımızda ilk başta 2.07 saniye gibi bir sürede geliyor ardından cache’de düşmüyor bunun sebebi unless kısmı sağlanmadığı için yaş 21 olduğundan cache düşmez ve yine 2 saniyenin üzerinde bir istekle geri döner.**


![](/images/5-cache.png)
![](/images/6-cache.png)

