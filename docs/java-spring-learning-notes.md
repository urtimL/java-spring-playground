## HTTP request → Controller

Frontend и backend — отдельные программы. Frontend не вызывает Java-методы Controller напрямую, а отправляет backend HTTP-запрос.

Пример:
```text
GET /api/v1/public/123/countries
```

- `GET` — HTTP method для получения данных.
- `/api/v1/public/123/countries` — path (путь запроса).

Spring Boot / Spring Web при запуске приложения находит Controller и их маршруты и регистрирует соответствие между HTTP-запросами и Java-методами.

Упрощённый процесс:
```text
Frontend
→ HTTP request
→ backend web server
→ Spring Web
→ поиск зарегистрированного маршрута
→ Controller
→ Java method
```

Например, маршрут:
```text
GET /api/v1/public/{cid}/countries
```

может быть связан Spring с:
```text
PublicCountryController.listAll()
```

`{cid}` — переменная часть URL: на её месте может находиться конкретное значение.

Важно: Controller сам не «слушает» HTTP-запросы. Spring обнаруживает Controller при запуске приложения и вызывает нужный метод, когда приходит подходящий запрос.


## package

`package` в Java определяет, к какой группе (пакету) относится класс.

Например:
```java
package io.cloudbooking.backend.controller.publicapi;
```

Для файла:
```text
src/main/java/io/cloudbooking/backend/controller/publicapi/PublicCountryController.java
```

структура каталогов соответствует имени package:
```text
io/
└── cloudbooking/
    └── backend/
        └── controller/
            └── publicapi/
```

Package — не просто папка. Он является частью полного имени Java-класса.

Короткое имя:
```text
PublicCountryController
```

Полное имя:
```text
io.cloudbooking.backend.controller.publicapi.PublicCountryController
```

Поэтому в разных package могут существовать классы с одинаковым коротким именем — для Java это будут разные классы.

В Booktech package также помогает понять архитектурное расположение класса:

```text
io.cloudbooking.backend.controller.publicapi
                         ↓
                    controller
                         ↓
                     publicapi
```

То есть `PublicCountryController` относится к слою Controller и находится в области public API.

`package` обычно указывается первой строкой Java-файла.

`;` в конце завершает инструкцию Java.



## import

`import` в Java позволяет использовать короткое имя класса вместо его полного имени.

Например:
```java
import io.cloudbooking.backend.common.dto.CountryDto;
```

Здесь:

```text
io.cloudbooking.backend.common.dto
```

— package,

а:

```text
CountryDto
```

— имя класса.

Вместе:

```text
io.cloudbooking.backend.common.dto.CountryDto
```

— полное имя класса.

После `import` внутри текущего Java-файла можно писать:

```java
CountryDto
```

вместо:

```java
io.cloudbooking.backend.common.dto.CountryDto
```

Важно:

- `import` не копирует класс в текущий файл;
- `import` не загружает его из интернета или другого проекта;
- он только позволяет обращаться к доступному классу по короткому имени.

В обычном Java `import` нельзя задать другое локальное имя (alias) для класса.

Если есть два класса с одинаковым коротким именем из разных package, оба нельзя импортировать так, чтобы дать им разные alias. Обычно один импортируют, а второй при необходимости используют по полному имени.


## DTO

`DTO` = `Data Transfer Object`.

Это объект, предназначенный для передачи данных между частями системы.

В backend DTO часто используется для передачи данных наружу, например в HTTP response для frontend.

Упрощённо:

```text
Database / внутренние данные
        ↓
      backend
        ↓
       DTO
        ↓
   HTTP response
        ↓
     frontend
```

DTO позволяет отделить внутреннюю структуру backend от данных, которые предоставляет API.

Например, внутри backend объект страны может теоретически содержать много полей:

```text
id
code
name
createdAt
updatedAt
internalStatus
...
```

а frontend могут быть нужны только:

```text
code
name
```

Тогда backend может сформировать DTO только с необходимыми данными.

Важно:

- DTO не обязательно соответствует структуре таблицы базы данных;
- DTO описывает данные, которые нужно передать;
- внутренняя структура backend и внешний API благодаря DTO могут изменяться более независимо друг от друга.

Типичная схема:

```text
Entity / внутренний объект
        ↓
  преобразование
        ↓
       DTO
        ↓
     frontend
```

`Entity` и механизм преобразования (`Mapper`) будут разобраны отдельно позже.

В Booktech:

```java
import io.cloudbooking.backend.common.dto.CountryDto;
```

означает, что `PublicCountryController` использует DTO `CountryDto`, расположенный в package:

```text
io.cloudbooking.backend.common.dto
```

Что именно содержит `CountryDto`, нужно определять по реальному классу `CountryDto`, а не по его названию.


## Service

`Service` — это слой приложения, в котором обычно находится логика выполнения задачи.

Упрощённая схема:

```text
HTTP request
→ Controller
→ Service
→ Repository / другие компоненты
```

Роли слоёв:

```text
Controller
→ принимает HTTP-запрос и передаёт работу дальше

Service
→ решает, что нужно сделать для выполнения задачи

Repository
→ работает с данными, обычно с базой данных
```

Контроллер стараются не перегружать бизнес-логикой, запросами к БД, преобразованием данных и другими деталями.

Например, вместо того чтобы самому получать список стран, `PublicCountryController` вызывает:

```java
countryService.findAllAsDto();
```

или:

```java
countryService.findAllAsLabel();
```

или:

```java
countryService.findByCode(code);
```

То есть связь в Booktech выглядит так:

```text
PublicCountryController
        ↓
   CountryService
```

Один Service может возвращать данные одной предметной области в разных представлениях.

Например:

```text
Country
  ↓
CountryDto
или
CountryLabelDto
```

`Service` — не специальное ключевое слово Java. Это архитектурная роль класса.

В Spring Service-классы часто отмечаются аннотацией:

```java
@Service
```

но наличие этой аннотации в конкретном классе нужно проверять по реальному коду.


## Поля класса, `private`, `final`, `static`

Пример из Booktech:

```java
private final CountryService countryService;
```

Здесь объявляется поле класса:

```text
countryService
```

его тип:

```text
CountryService
```

---

### Поле класса

Поле — это переменная, которая хранится внутри объекта класса.

Например:

```java
class User {
    int age;
}
```

Если создать несколько объектов:

```java
User user1 = new User();
User user2 = new User();
```

то у каждого будет собственное поле:

```text
user1 → age
user2 → age
```

---

### `private`

`private` — модификатор доступа.

Он означает, что поле доступно только внутри самого класса.

```java
private int age;
```

То есть другой класс не должен напрямую обращаться к этому полю.

---

### `final`

`final` означает, что переменной можно присвоить значение только один раз.

Например:

```java
final int x;
x = 10;
```

это допустимо.

После этого:

```java
x = 20;
```

уже нельзя.

Важно: `final` не требует обязательно указывать значение прямо в строке объявления. Значение может быть присвоено позже, например в конструкторе.

Для поля класса:

```java
class Test {
    final int x;

    Test(int value) {
        x = value;
    }
}
```

Каждый объект получает своё значение `x`, но после создания объекта изменить его уже нельзя.

---

### `final` для объектов

Если:

```java
final SomeObject obj = ...;
```

то `final` запрещает переназначить ссылку:

```text
obj → object A
```

нельзя заменить на:

```text
obj → object B
```

Но сам `object A` не обязательно становится неизменяемым.

Поэтому:

```text
final reference
≠
immutable object
```

---

### `static`

Обычное поле принадлежит каждому объекту отдельно:

```java
class User {
    int age;
}
```

```text
user1 → age = 20
user2 → age = 35
```

`static` делает поле принадлежащим самому классу:

```java
class User {
    static int maxAge;
}
```

Теперь существует одно общее поле:

```text
User
└── maxAge
```

а не отдельное `maxAge` у каждого объекта.

К `static`-полю обычно обращаются через имя класса:

```java
User.maxAge
```

---

### `static final`

`static` и `final` отвечают на разные вопросы:

```text
static
→ кому принадлежит поле?
→ классу

final
→ можно ли переназначить значение?
→ нет
```

Вместе:

```java
static final int MAX_AGE = 100;
```

означает:

- поле принадлежит классу;
- существует одно общее значение;
- это значение нельзя переназначить.

Поэтому `static final` обычно используют для констант.

По соглашению Java константы пишут большими буквами:

```java
MAX_AGE
DEFAULT_TIMEOUT
API_VERSION
```

Сравнение:

```text
int x
→ поле объекта
→ можно менять

final int x
→ поле объекта
→ после первого присваивания нельзя переназначить

static int x
→ одно общее поле класса
→ можно менять

static final int x
→ одно общее поле класса
→ нельзя переназначить
```

---

### В Booktech

```java
private final CountryService countryService;
```

означает:

- `countryService` — поле объекта `PublicCountryController`;
- тип поля — `CountryService`;
- `private` — поле используется только внутри `PublicCountryController`;
- `final` — после первоначального присваивания нельзя заменить ссылку на другой `CountryService`;
- `static` отсутствует, значит поле принадлежит конкретному объекту Controller, а не самому классу.


## Конструктор, Lombok, Spring DI и объекты

### Конструктор

Конструктор — специальный метод, который вызывается при создании объекта.

Пример:

```java
public class PublicCountryController {

    private final CountryService countryService;

    public PublicCountryController(CountryService countryService) {
        this.countryService = countryService;
    }
}
```

Конструктор здесь:

```java
public PublicCountryController(CountryService countryService) {
    this.countryService = countryService;
}
```

Имя конструктора совпадает с именем класса.

```java
this.countryService = countryService;
```

означает:

- `this.countryService` — поле текущего объекта;
- `countryService` справа — параметр конструктора.

Конструктор получает объект `CountryService` и сохраняет ссылку на него в поле Controller.

---

### Lombok `@RequiredArgsConstructor`

Lombok — сторонняя Java-библиотека, которая может автоматически генерировать шаблонный код.

Импорт:

```java
import lombok.RequiredArgsConstructor;
```

только делает аннотацию доступной в текущем файле.

Аннотация:

```java
@RequiredArgsConstructor
```

над классом заставляет Lombok автоматически создать конструктор для обязательных полей, в частности `final`-полей.

Поэтому:

```java
@RequiredArgsConstructor
public class PublicCountryController {

    private final CountryService countryService;
}
```

примерно эквивалентно:

```java
public class PublicCountryController {

    private final CountryService countryService;

    public PublicCountryController(CountryService countryService) {
        this.countryService = countryService;
    }
}
```

Lombok не создаёт объект `PublicCountryController`.

Он только генерирует код конструктора.

---

### Что делает Spring

Без Spring объект пришлось бы создавать вручную:

```java
PublicCountryController controller =
    new PublicCountryController(countryService);
```

Если класс помечен:

```java
@RestController
```

Spring сам обнаруживает этот класс и создаёт объект `PublicCountryController`.

При создании Spring видит конструктор:

```java
PublicCountryController(CountryService countryService)
```

и понимает, что для создания Controller нужен объект типа `CountryService`.

Spring находит подходящий `CountryService` bean и передаёт его в конструктор.

Упрощённо:

```text
Spring
↓
находит PublicCountryController
↓
видит необходимый CountryService
↓
находит CountryService bean
↓
создаёт PublicCountryController
↓
передаёт CountryService в конструктор
```

---

### Dependency Injection

`Dependency` = зависимость.

`PublicCountryController` зависит от `CountryService`, потому что использует его методы.

`Injection` = передача этой зависимости объекту извне.

Вместо:

```java
new CountryService()
```

внутри Controller:

```java
private final CountryService countryService;
```

а готовый объект `CountryService` передаёт Spring.

Это называется Dependency Injection.

Важно:

```text
import
→ сообщает Java, какой класс обозначает имя CountryService

Spring DI
→ предоставляет конкретный объект CountryService
```

`import` не создаёт объект.

---

### Разделение ролей Spring и Lombok

Для одной и той же конструкции:

```java
private final CountryService countryService;
```

роли разделяются так:

```text
Java
→ описывает поле и класс

Lombok
→ генерирует конструктор

Spring
→ создаёт объект Controller
→ находит зависимость
→ передаёт CountryService в конструктор
```

---

### Три варианта

#### 1. Чистая Java

```java
public class PublicCountryController {

    private final CountryService countryService;

    public PublicCountryController(CountryService countryService) {
        this.countryService = countryService;
    }
}
```

Конструктор пишем сами.

Объект тоже создаём сами:

```java
new PublicCountryController(countryService);
```

#### 2. Java + Spring

```java
@RestController
public class PublicCountryController {

    private final CountryService countryService;

    public PublicCountryController(CountryService countryService) {
        this.countryService = countryService;
    }
}
```

Конструктор пишем сами.

Объект `PublicCountryController` создаёт Spring.

#### 3. Java + Spring + Lombok

```java
@RestController
@RequiredArgsConstructor
public class PublicCountryController {

    private final CountryService countryService;
}
```

Lombok генерирует конструктор.

Spring создаёт объект и передаёт в него `CountryService`.

---

## Класс и объект

Класс — описание структуры и поведения.

Объект — конкретный экземпляр этого класса, существующий во время выполнения программы.

Например:

```java
class User {
    String name;
}
```

`User` — класс.

```java
User user1 = new User();
User user2 = new User();
```

`user1` и `user2` — два разных объекта одного класса.

Они могут хранить разные данные.

---

## Когда объект не нужен

Java не требует использовать объект для каждой функции.

Если метод не зависит от состояния объекта и общих зависимостей, его можно сделать `static`.

Например:

```java
public class MathUtil {

    public static int add(int a, int b) {
        return a + b;
    }
}
```

Использование:

```java
MathUtil.add(2, 3);
```

Объект `MathUtil` создавать не нужно.

То есть в Java возможны оба подхода:

```text
нужна просто функция
→ static method

нужно состояние или набор общих зависимостей
→ object + instance methods
```

---

## Почему Spring Service обычно является объектом

Service может не иметь собственного изменяемого состояния, но иметь зависимости:

```text
CountryService
├── CountryRepository
├── Mapper
└── другие Service
```

Spring один раз создаёт объект `CountryService` и передаёт ему нужные зависимости.

После этого методы Service могут использовать их без постоянной передачи параметрами:

```java
countryService.findAllAsDto();
countryService.findByCode(code);
```

Альтернативный процедурный подход тоже возможен:

```java
CountryFunctions.findAllAsDto(
    repository,
    mapper,
    languageService
);
```

Но тогда общие зависимости приходится передавать во многие функции снова и снова.

Spring предпочитает управляемые объекты (`beans`), потому что может централизованно:

- создавать их;
- внедрять зависимости;
- управлять их жизненным циклом;
- связывать компоненты приложения.

Важно:

```text
Java допускает как объектный, так и static/процедурный стиль.

Spring в основном строит архитектуру приложения вокруг управляемых объектов.
```

### Дополнение: когда `import` не нужен

Если два класса находятся в одном и том же `package`, импортировать один класс в другой не нужно.

Например:

```java
package io.cloudbooking.backend.common.service;
```

есть и у `CountryService`, и у `CommonDataCache`.

Поэтому в `CountryService` можно писать:

```java
private final CommonDataCache commonDataCache;
```

без:

```java
import io.cloudbooking.backend.common.service.CommonDataCache;
```

Если класс находится в другом `package`, тогда `import` обычно нужен.

Пример:

```java
import io.cloudbooking.backend.common.mapper.CountryMapper;
```

потому что `CountryMapper` находится в package:

```text
io.cloudbooking.backend.common.mapper
```

## Spring bean и Dependency Injection

Spring управляет объектами приложения, которые зарегистрированы как Spring components.

Такой объект обычно называют `bean`.

Например:

```java
@Service
public class CountryService {
```

Аннотация:

```java
@Service
```

говорит Spring, что `CountryService` является сервисным компонентом приложения и должен управляться Spring.

Spring создаёт объект `CountryService` и может передавать его другим компонентам как зависимость.

---

### Dependency Injection

Если один объект зависит от другого:

```text
PublicCountryController
        ↓
   CountryService
```

то Controller не обязан сам создавать Service через:

```java
new CountryService(...)
```

Вместо этого Spring передаёт готовый объект `CountryService` в конструктор Controller.

Упрощённо:

```text
Spring создаёт CountryService
↓
Spring создаёт PublicCountryController
↓
передаёт CountryService в конструктор Controller
```

Это называется Dependency Injection.

Важно:

```text
import
→ сообщает Java, какой класс имеется в виду

Dependency Injection
→ предоставляет конкретный объект этого класса
```

---

## Зависимости могут образовывать цепочку

`CountryService` сам имеет зависимости:

```java
private final CommonDataCache commonDataCache;
private final CountryMapper countryMapper;
```

То есть:

```text
PublicCountryController
        ↓
   CountryService
      ↓       ↓
CommonDataCache
CountryMapper
```

Spring может создавать и связывать такие компоненты автоматически.

---

## Mapper

Mapper — компонент, который преобразует объект одного типа в объект другого типа.

В Booktech:

```text
Country
↓
CountryMapper
↓
CountryDto
```

или:

```text
Country
↓
CountryMapper
↓
CountryLabelDto
```

`CountryMapper` объявлен как:

```java
@Mapper(componentModel = "spring")
```

Это означает, что MapStruct генерирует реализацию Mapper, а Spring управляет ею как bean.

Основные методы:

```java
CountryLabelDto toLabelDto(Country country);

CountryDto toDto(Country country);

List<CountryLabelDto> toLabelDtoList(List<Country> countries);

List<CountryDto> toDtoList(List<Country> countries);
```

---

## Cache

Cache — промежуточное хранилище данных, позволяющее не обращаться к базе данных каждый раз.

В Booktech `CountryService` получает страны через:

```java
commonDataCache.getCountries()
```

а не напрямую через `CountryRepository`.

`CommonDataCache` заранее загружает страны через Repository и хранит их для последующего использования.

Упрощённо:

```text
Database
↓
CountryRepository
↓
CommonDataCache
↓
CountryService
```

---

## Реальный метод `findAllAsDto()`

```java
public List<CountryDto> findAllAsDto() {
    return countryMapper.toDtoList(
        new ArrayList<>(commonDataCache.getCountries().values())
    );
}
```

Логика:

```text
commonDataCache.getCountries()
↓
получить страны из cache

.values()
↓
получить объекты Country

new ArrayList<>(...)
↓
преобразовать их в List<Country>

countryMapper.toDtoList(...)
↓
преобразовать Country → CountryDto

return
↓
вернуть List<CountryDto>
```

Полная подтверждённая цепочка для получения списка стран:

```text
Database
↓
CountryRepository
↓
CommonDataCache
↓
CountryService
↓
CountryMapper
↓
CountryDto
↓
PublicCountryController
↓
HTTP response
```

## `Map<K, V>`

`Map` — структура данных Java, которая хранит элементы в виде пар:

```text
ключ → значение
```

Например:

```text
"NO" → Country Norway
"SE" → Country Sweden
"UA" → Country Ukraine
```

В Booktech метод:

```java
commonDataCache.getCountries()
```

возвращает:

```java
Map<String, Country>
```

Это означает:

```text
String
→ тип ключа

Country
→ тип значения
```

То есть страны хранятся примерно как:

```text
код страны → объект Country
```

В `CommonDataCache` ключом действительно является код страны:

```java
countries.put(c.getCode(), c);
```

`Map` отличается от обычного списка тем, что значение можно связать с конкретным ключом.

---

## Generics `<T>`, `<K, V>`

Generics позволяют указывать, с какими типами данных должен работать класс, интерфейс или метод.

Например:

```java
List<Country>
```

означает:

```text
List
→ список

Country
→ тип элементов этого списка
```

А:

```java
Map<String, Country>
```

содержит два типа:

```text
String
→ тип ключа

Country
→ тип значения
```

В общем виде это часто обозначают:

```java
Map<K, V>
```

где:

```text
K = Key
V = Value
```

Для Booktech:

```text
K = String
V = Country
```

Generics позволяют Java проверять типы элементов ещё при компиляции.

Например, в:

```java
Map<String, Country>
```

ключ должен соответствовать `String`, а значение — `Country`.

---

## `Map.values()`

У `Map` есть метод:

```java
values()
```

который возвращает только значения Map без ключей.

Например, было:

```text
"NO" → Country Norway
"SE" → Country Sweden
"UA" → Country Ukraine
```

после:

```java
.values()
```

остаются:

```text
Country Norway
Country Sweden
Country Ukraine
```

Для:

```java
Map<String, Country>
```

выражение:

```java
map.values()
```

возвращает:

```java
Collection<Country>
```

То есть:

```text
Map<String, Country>
↓ .values()
Collection<Country>
```

---

## `Collection`

`Collection` — общий интерфейс Java для группы элементов.

Упрощённая иерархия:

```text
Collection
├── List
├── Set
└── Queue
```

Это означает, что `List`, `Set` и `Queue` — разные виды коллекций.

`Collection` определяет общие операции, например:

```text
add()
remove()
size()
isEmpty()
contains()
```

Но сам `Collection` не определяет конкретный способ организации и хранения элементов.

Например:

```java
Collection<Country>
```

означает:

```text
некоторая коллекция объектов Country
```

но из этого типа ещё не следует, является ли она списком, множеством или другой разновидностью коллекции.

---

## `List`

`List` — интерфейс для упорядоченной коллекции элементов.

Например:

```java
List<Country>
```

означает список объектов `Country`.

Для `List` характерны:

```text
сохраняется порядок элементов
есть позиции / индексы
дубликаты разрешены
элемент можно получить по его индексу
```

Например:

```text
index 0 → Country Norway
index 1 → Country Sweden
index 2 → Country Ukraine
```

`List` является более конкретным типом `Collection`:

```text
Collection
↓
List
```

Но `List` всё ещё является интерфейсом и сам не определяет конкретный способ хранения элементов.

---

## `ArrayList`

`ArrayList` — конкретный Java-класс, реализующий интерфейс `List`.

Объект создаётся через:

```java
new ArrayList<>()
```

`ArrayList` хранит элементы на основе динамического массива.

Упрощённо:

```text
[A][B][C][D]
```

В отличие от обычного массива, размером `ArrayList` управляет сам: при добавлении новых элементов внутреннее хранилище при необходимости расширяется.

Связь типов:

```text
Collection
↓
List
↓
ArrayList
```

Поэтому:

```text
ArrayList является List
List является Collection
```

Но обратное неверно:

```text
не каждый List является ArrayList
```

Например, другая реализация `List`:

```text
LinkedList
```

---

## Интерфейс и реализация коллекции

В Java часто пишут:

```java
List<Country> countries = new ArrayList<>();
```

Здесь:

```text
List<Country>
→ тип переменной

ArrayList
→ конкретный класс созданного объекта
```

То есть переменная объявлена через общий интерфейс `List`, а реальный объект создаётся как `ArrayList`.

Упрощённо:

```text
List
→ что объект должен уметь

ArrayList
→ как это реализовано
```

Такой подход уменьшает зависимость кода от конкретной реализации.

Например:

```java
List<Country> countries = new ArrayList<>();
```

теоретически можно заменить на:

```java
List<Country> countries = new LinkedList<>();
```

если остальному коду достаточно методов интерфейса `List`.

Поэтому методы тоже часто принимают общий тип:

```java
List<Country>
```

а не конкретно:

```java
ArrayList<Country>
```

В Booktech `CountryMapper` принимает:

```java
List<CountryDto> toDtoList(List<Country> countries);
```

Поэтому ему можно передать `ArrayList<Country>`, поскольку `ArrayList` реализует `List`.

---

## Преобразование `Collection` в `List` в Booktech

Выражение:

```java
commonDataCache.getCountries()
```

возвращает:

```java
Map<String, Country>
```

Затем:

```java
commonDataCache.getCountries().values()
```

возвращает:

```java
Collection<Country>
```

Но метод:

```java
countryMapper.toDtoList(...)
```

ожидает:

```java
List<Country>
```

Поэтому используется:

```java
new ArrayList<>(commonDataCache.getCountries().values())
```

Конструктор `ArrayList` получает существующую `Collection<Country>` и создаёт новый список с теми же элементами.

Полная последовательность:

```text
commonDataCache.getCountries()
↓
Map<String, Country>

.values()
↓
Collection<Country>

new ArrayList<>(...)
↓
ArrayList<Country>

ArrayList реализует List
↓
можно передать как List<Country>
```


## Структура Spring Boot-проекта

Spring Boot-приложение является самостоятельным Java-проектом.

Типичная структура проекта:

```text
project/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── gradle/
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    └── test/
        └── java/
```

Основные части:

```text
src/main/java
→ основной Java-код приложения

src/main/resources
→ конфигурация и другие ресурсы приложения

src/test/java
→ тестовый Java-код

build.gradle.kts
→ конфигурация сборки и зависимостей Gradle

settings.gradle.kts
→ настройки самого Gradle-проекта
```

---

## Gradle

Gradle — система сборки проекта.

Он не является частью Java или Spring Boot.

Gradle отвечает за:

```text
компиляцию Java-кода
скачивание зависимостей
запуск тестов
сборку приложения
запуск Spring Boot-приложения
```

Упрощённо:

```text
build.gradle.kts
↓
Gradle читает конфигурацию
↓
скачивает зависимости
↓
компилирует Java
↓
запускает тесты / собирает приложение
```

---

## `build.gradle.kts`

`build.gradle.kts` — основной файл конфигурации Gradle для проекта.

Расширение:

```text
.kts
```

означает Kotlin DSL.

То есть конфигурация Gradle записана на синтаксисе Kotlin, хотя само приложение написано на Java.

Основные блоки:

```text
plugins
→ подключаемые возможности Gradle

group / version
→ идентификация проекта

java
→ настройки Java

repositories
→ откуда получать библиотеки

dependencies
→ какие библиотеки нужны проекту

tasks
→ настройка Gradle-задач
```

---

## Gradle plugins

Пример:

```kotlin
plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
}
```

### `java`

Подключает стандартный Java plugin Gradle.

После этого Gradle понимает стандартную структуру:

```text
src/main/java
src/test/java
```

и получает Java-задачи, например:

```text
compileJava
test
classes
jar
```

---

### Spring Boot Gradle plugin

```kotlin
id("org.springframework.boot") version "4.1.1"
```

добавляет Gradle поддержку Spring Boot.

Например, появляется задача:

```bash
./gradlew bootRun
```

для запуска Spring Boot-приложения.

Упрощённо:

```text
Java plugin
→ умеет собирать Java

Spring Boot plugin
→ добавляет возможности для Spring Boot-приложения
```

---

### Dependency Management plugin

```kotlin
id("io.spring.dependency-management") version "1.1.7"
```

помогает использовать согласованные версии Spring-зависимостей.

Поэтому зависимость можно указывать без собственной версии:

```kotlin
implementation("org.springframework.boot:spring-boot-starter-webmvc")
```

Совместимую версию определяет Spring dependency management.

---

## `group` и `version`

Пример:

```kotlin
group = "dev.timur"
version = "0.0.1-SNAPSHOT"
```

`group` — логическое пространство имён проекта.

Например:

```text
com.company
org.project
io.cloudbooking
dev.timur
```

`version` — версия создаваемого приложения.

```text
0.0.1-SNAPSHOT
```

означает раннюю разрабатываемую версию.

`SNAPSHOT` показывает, что версия ещё не считается окончательным стабильным релизом.

При сборке версия может войти в имя файла:

```text
java-spring-playground-0.0.1-SNAPSHOT.jar
```

---

## Java Toolchain

Пример:

```kotlin
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
```

Gradle Toolchain определяет, какую версию Java должен использовать проект.

Это позволяет отделить:

```text
какие JDK установлены на компьютере

от

какая Java требуется конкретному проекту
```

Для данного проекта:

```text
Java 25
```

---

## `repositories`

Пример:

```kotlin
repositories {
    mavenCentral()
}
```

`repositories` определяет, где Gradle должен искать внешние библиотеки.

`mavenCentral()` означает:

```text
использовать Maven Central Repository
```

Важно различать:

```text
Gradle
→ система сборки

Maven Central
→ хранилище Java-библиотек
```

Gradle может скачивать зависимости из Maven Central, не используя Maven как систему сборки.

---

## Dependencies

Dependency — внешняя библиотека, от которой зависит проект.

Пример:

```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```

Типы зависимостей:

```text
implementation
→ нужна основному коду приложения

testImplementation
→ нужна тестовому коду

testRuntimeOnly
→ нужна только во время выполнения тестов
```

---

## Spring Boot Starter

Starter — готовый набор связанных зависимостей для определённой задачи.

Например:

```kotlin
implementation("org.springframework.boot:spring-boot-starter-webmvc")
```

подключает набор библиотек для Spring MVC / web-приложения.

Упрощённо:

```text
spring-boot-starter-webmvc
↓
Spring Web / MVC
JSON support
Servlet infrastructure
embedded web server
и связанные зависимости
```

Одна starter-зависимость может транзитивно подключить множество других библиотек.

---

## Gradle tasks

Gradle выполняет работу через tasks.

Примеры:

```text
compileJava
test
build
bootRun
```

Пример настройки:

```kotlin
tasks.withType<Test> {
    useJUnitPlatform()
}
```

означает:

```text
для Gradle-задач типа Test
использовать JUnit Platform
```

JUnit используется для автоматических Java-тестов.

---

## Главный класс Spring Boot-приложения

Пример:

```java
@SpringBootApplication
public class JavaSpringPlaygroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaSpringPlaygroundApplication.class, args);
    }
}
```

Этот класс является точкой запуска Spring Boot-приложения.

---

## `@SpringBootApplication`

```java
@SpringBootApplication
```

— главная аннотация Spring Boot-приложения.

Она указывает основной класс конфигурации приложения.

Упрощённо:

```text
@SpringBootApplication
↓
главная конфигурация Spring Boot
↓
автоматическая настройка
↓
поиск Spring-компонентов
```

Spring начинает поиск компонентов от package, в котором расположен главный класс, и его подпакетов.

Например, если главный класс расположен в:

```text
dev.timur.playground
```

то компоненты могут находиться в:

```text
dev.timur.playground.health
dev.timur.playground.service
dev.timur.playground.api
dev.timur.playground.database
```

Поэтому главный Spring Boot-класс обычно размещают достаточно высоко в иерархии package.

---

## `main(String[] args)`

Стандартная точка входа Java-программы:

```java
public static void main(String[] args)
```

Здесь:

```text
public
→ JVM может вызвать метод

static
→ для запуска не нужно заранее создавать объект класса

void
→ метод ничего не возвращает

main
→ стандартное имя точки входа

String[] args
→ аргументы командной строки
```

Например:

```bash
java MyApp one two
```

может передать:

```text
args[0] = "one"
args[1] = "two"
```

---

## `SpringApplication.run()`

Реальный запуск Spring Boot происходит здесь:

```java
SpringApplication.run(JavaSpringPlaygroundApplication.class, args);
```

Упрощённый процесс:

```text
JVM
↓
main()
↓
SpringApplication.run(...)
↓
Spring Boot запускается
↓
создаётся Spring context
↓
ищутся Spring components
↓
создаются beans
↓
внедряются зависимости
↓
выполняется auto-configuration
↓
приложение готово к работе
```

Именно на этом этапе начинают работать механизмы, которые ранее встречались в Booktech:

```java
@Service
@Component
@RestController
```

Spring обнаруживает соответствующие классы и создаёт управляемые объекты — beans.

---

## `.class`

В выражении:

```java
JavaSpringPlaygroundApplication.class
```

`.class` не означает файл с расширением `.class`.

Это Java-конструкция, которая предоставляет объект с информацией о конкретном классе.

Здесь Spring Boot получает информацию о:

```text
JavaSpringPlaygroundApplication
```

и использует его как главный класс конфигурации приложения.

Упрощённо:

```text
JavaSpringPlaygroundApplication.class
↓
передать Spring информацию о главном Java-классе
```

---

## Общий запуск Spring Boot-приложения

```text
запуск Java-программы
↓
main()
↓
SpringApplication.run()
↓
создание Spring context
↓
сканирование package
↓
обнаружение компонентов
↓
создание beans
↓
Dependency Injection
↓
приложение работает
```

Это начало процесса, который в Booktech далее приводит к созданию таких компонентов, как:

```text
Controller
Service
Repository
Mapper
другие Spring beans
```

## Запуск Spring Boot через Gradle Wrapper

Команда:

```bash
./gradlew bootRun
```

состоит из двух частей:

```text
./gradlew
→ запустить Gradle Wrapper текущего проекта

bootRun
→ выполнить Gradle-задачу запуска Spring Boot-приложения
```

`gradlew` — это Gradle Wrapper.

Он позволяет использовать ту версию Gradle, которая задана для конкретного проекта, без необходимости полагаться на глобально установленный Gradle.

Задача:

```text
bootRun
```

появляется благодаря Spring Boot Gradle plugin:

```kotlin
id("org.springframework.boot")
```

Упрощённый процесс запуска:

```text
./gradlew bootRun
↓
Gradle Wrapper
↓
Gradle
↓
Spring Boot plugin
↓
компиляция проекта
↓
поиск главного класса
↓
main()
↓
SpringApplication.run(...)
↓
Spring Boot-приложение запущено
```

Важно:

`bootRun` не заменяет метод:

```java
public static void main(String[] args)
```

Он только организует запуск приложения через Gradle.

После сборки приложение также можно запускать как `jar`:

```bash
./gradlew build
java -jar build/libs/имя-файла.jar
```

Для разработки `bootRun` удобнее, потому что позволяет сразу запускать приложение из проекта.

---

### Проверка среды проекта

Перед началом работы мы проверили Java и Gradle в терминале.

Команда:

```bash
java -version
```

показала установленную Java:

```text
OpenJDK 25.0.4.1
```

Команда:

```bash
javac -version
```

показала Java compiler:

```text
javac 25.0.4.1
```

Это означает, что установлен полноценный JDK, а не только Java Runtime.

Для проверки Gradle Wrapper использовалась команда из корня проекта:

```bash
./gradlew --version
```

Она показала:

```text
Gradle 9.7.1
Launcher JVM: Java 25.0.4.1
Daemon JVM: Java 25
```

То есть Gradle действительно использует Java 25.

Команда:

```bash
./gradlew tasks
```

успешно прочитала конфигурацию проекта и показала доступные задачи, включая:

```text
bootRun
build
test
bootJar
```

Результат:

```text
BUILD SUCCESSFUL
```

Это подтверждает, что:

```text
build.gradle.kts
↓
успешно читается Gradle

Java Toolchain
↓
работает

Spring Boot Gradle plugin
↓
подключён

проект
↓
корректно распознаётся как Spring Boot-проект
```

---

### Проверка запуска Spring Boot

Проект был запущен командой:

```bash
./gradlew bootRun
```

В логе появились строки:

```text
Spring Boot 4.1.1
Java 25.0.4.1
Tomcat initialized with port 8080
Tomcat started on port 8080
Started JavaSpringPlaygroundApplication
```

Это подтверждает реальный запуск приложения.

Упрощённо произошёл процесс:

```text
./gradlew bootRun
↓
Gradle запускает приложение
↓
main()
↓
SpringApplication.run(...)
↓
создаётся Spring context
↓
выполняется auto-configuration
↓
запускается встроенный Tomcat
↓
приложение готово к работе
```

---

### Встроенный Tomcat

После подключения web starter Spring Boot автоматически запускает встроенный web server.

В нашем проекте был запущен:

```text
Apache Tomcat 11
```

на порту:

```text
8080
```

Это означает, что приложение сейчас работает как web-приложение и может принимать HTTP-запросы.

Tomcat не устанавливался отдельно.

Он входит в зависимости Spring Boot web-приложения и запускается автоматически при старте приложения.

---

### Почему `bootRun` может показывать `80% EXECUTING`

Во время работы Spring Boot-приложения Gradle-задача:

```text
bootRun
```

остаётся активной.

Поэтому Gradle может показывать, например:

```text
80% EXECUTING
> :bootRun
```

даже если Spring Boot уже полностью запущен.

Это не означает, что приложение загрузилось только на 80%.

Признаком успешного запуска являются сообщения Spring Boot, например:

```text
Started JavaSpringPlaygroundApplication
Tomcat started on port 8080
```

Пока приложение работает:

```text
bootRun
↓
остаётся активной Gradle-задачей
↓
Gradle продолжает показывать EXECUTING
```

Остановить приложение можно сочетанием:

```text
Ctrl+C
```

После этого процесс Spring Boot завершается и терминал снова возвращается к обычной командной строке.

## HTTP-клиент `RestClient` в Spring Boot

Для обращения из нашего приложения к внешнему HTTP API используется:

```java
RestClient
```

`RestClient` — HTTP-клиент Spring, который позволяет программе самой отправлять HTTP-запросы.

То есть в этой задаче наше приложение работает не как сервер, принимающий запрос, а как клиент другого API:

```text
java-spring-playground
↓
HTTP GET
↓
Cloudbooking API
↓
HTTP response
```

---

## `HealthStatusService`

Для работы с health API создан отдельный Spring Service:

```java
@Service
public class HealthStatusService {
}
```

Аннотация:

```java
@Service
```

говорит Spring, что этот класс является Spring-компонентом и объект этого класса должен создаваться и управляться Spring.

Класс расположен в package:

```text
dev.timur.playground.health
```

который является подпакетом:

```text
dev.timur.playground
```

Поэтому Spring находит его при component scanning, который начинается от package главного класса с:

```java
@SpringBootApplication
```

---

## Создание `RestClient` в `HealthStatusService`

В `HealthStatusService` используется поле:

```java
private final RestClient restClient;
```

Первоначально была попытка получить `RestClient.Builder` через Dependency Injection:

```java
public HealthStatusService(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.build();
}
```

Но при запуске приложение завершилось ошибкой:

```text
Parameter 0 of constructor in HealthStatusService
required a bean of type 'RestClient$Builder'
that could not be found
```

То есть в текущей конфигурации проекта Spring не создал bean типа:

```java
RestClient.Builder
```

Поэтому для этой простой учебной задачи `RestClient` был создан непосредственно в конструкторе:

```java
public HealthStatusService() {
    this.restClient = RestClient.create();
}
```

Теперь процесс выглядит так:

```text
Spring
↓
обнаруживает @Service
↓
создаёт HealthStatusService
↓
вызывается его конструктор
↓
RestClient.create()
↓
создаётся RestClient
↓
ссылка сохраняется в поле restClient
```

В данном случае Spring создаёт объект:

```text
HealthStatusService
```

но объект:

```text
RestClient
```

создаётся уже самим кодом `HealthStatusService`.

Это отличается от Dependency Injection:

```text
Dependency Injection
→ Spring передаёт готовую зависимость извне

RestClient.create()
→ класс сам создаёт нужный объект
```

---

## HTTP-запрос через `RestClient`

Запрос к API выполняется методом:

```java
public HealthStatusResponse getHealthStatusResponse() {
    return restClient
            .get()
            .uri("https://api.test.cloudbooking.io/api/v1/health/status")
            .retrieve()
            .body(HealthStatusResponse.class);
}
```

Последовательность:

```text
restClient
↓
.get()
→ подготовить HTTP GET request
↓
.uri(...)
→ указать адрес API
↓
.retrieve()
→ выполнить запрос и перейти к обработке response
↓
.body(HealthStatusResponse.class)
→ преобразовать JSON body в Java-объект
↓
return
→ вернуть объект HealthStatusResponse
```

---

## Method chaining

Конструкция:

```java
restClient
        .get()
        .uri(...)
        .retrieve()
        .body(...)
```

называется method chaining — цепочка вызовов методов.

Каждый следующий метод вызывается на объекте, который вернул предыдущий метод.

Упрощённо:

```text
restClient
↓
.get()
↓
объект настройки GET request
↓
.uri(...)
↓
настроенный request
↓
.retrieve()
↓
объект обработки HTTP response
↓
.body(...)
```

Такой синтаксис позволяет не создавать отдельную переменную для каждого промежуточного объекта.

---

## `.get()`

```java
.get()
```

указывает HTTP method:

```text
GET
```

То есть программа собирается получить данные с внешнего API.

---

## `.uri(...)`

```java
.uri("https://api.test.cloudbooking.io/api/v1/health/status")
```

задаёт адрес запроса.

Адрес:

```text
https://api.test.cloudbooking.io/api/v1/health/status
```

можно разделить:

```text
https
→ protocol

api.test.cloudbooking.io
→ host

/api/v1/health/status
→ API path
```

---

## `.retrieve()`

```java
.retrieve()
```

переходит от настройки HTTP request к выполнению запроса и обработке HTTP response.

Упрощённо:

```text
.get()
→ какой запрос

.uri(...)
→ куда

.retrieve()
→ выполнить запрос и получить response
```

HTTP response может содержать:

```text
HTTP status code
headers
body
```

---

## Проверка реального ответа API

До создания Java-модели ответ API был проверен напрямую командой:

```bash
curl -s https://api.test.cloudbooking.io/api/v1/health/status
```

Реальный JSON:

```json
{
  "environment": "test",
  "application": "CloudBooking Backend Application",
  "status": "UP",
  "timestamp": "2026-10-05T12:52:58.515612693"
}
```

Это важно: структуру DTO следует строить по фактическому ответу API, а не предполагать её только по названию полей в описании задания.

Структура ответа:

```text
JSON root
├── environment
├── application
├── status
└── timestamp
```

Поле:

```text
status
```

находится непосредственно на верхнем уровне JSON.

---

## `HealthStatusResponse`

Для представления JSON создан Java-класс:

```java
public class HealthStatusResponse {

    private String environment;
    private String application;
    private String status;
    private String timestamp;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
```

Java-структура соответствует JSON:

```text
JSON                         Java

environment                 environment
application                 application
status                      status
timestamp                   timestamp
```

Все четыре значения в полученном JSON представлены строками, поэтому для текущей задачи используется:

```java
String
```

для всех полей.

---

## JSON → Java object

Раньше body можно было получить просто как строку:

```java
.body(String.class)
```

Но для работы с отдельными полями удобнее сразу преобразовать JSON в Java-объект:

```java
.body(HealthStatusResponse.class)
```

Теперь:

```text
JSON body
↓
Spring / JSON converter
↓
HealthStatusResponse
```

Имена JSON-полей совпадают с именами Java-полей:

```text
"environment" → environment
"application" → application
"status"      → status
"timestamp"   → timestamp
```

После преобразования получается обычный Java-объект, у которого можно вызвать:

```java
response.getStatus()
```

и получить:

```text
UP
```

---

## Getter и Setter

Например:

```java
public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}
```

Setter позволяет записать значение в private-поле:

```text
"status": "UP"
↓
setStatus("UP")
↓
status = "UP"
```

Getter позволяет затем получить значение:

```java
response.getStatus()
↓
"UP"
```

---

## Метод `getStatus()`

В `HealthStatusService` добавлен метод:

```java
public String getStatus() {
    HealthStatusResponse response = getHealthStatusResponse();
    return response.getStatus();
}
```

Первая строка:

```java
HealthStatusResponse response = getHealthStatusResponse();
```

означает:

```text
вызвать getHealthStatusResponse()
↓
получить HealthStatusResponse
↓
сохранить ссылку на объект в переменной response
```

Здесь:

```text
HealthStatusResponse
→ тип переменной

response
→ имя переменной
```

Следующая строка:

```java
return response.getStatus();
```

вызывает getter объекта и возвращает только:

```text
status
```

То есть:

```text
HTTP GET
↓
JSON
↓
HealthStatusResponse
↓
getStatus()
↓
"UP"
```

---

## `CommandLineRunner`

По заданию значение `status` должно выводиться в standard output после запуска программы.

Для выполнения кода после запуска Spring Boot используется:

```java
CommandLineRunner
```

В главном классе добавлено:

```java
@Bean
CommandLineRunner run(HealthStatusService healthStatusService) {
    return args -> {
        String status = healthStatusService.getStatus();
        System.out.println(status);
    };
}
```

`CommandLineRunner` — Spring-интерфейс, позволяющий выполнить код после запуска Spring application context.

Упрощённо:

```text
SpringApplication.run(...)
↓
создаётся Spring context
↓
создаются beans
↓
Spring находит CommandLineRunner
↓
запускает его
```

---

## `@Bean` для `CommandLineRunner`

Метод:

```java
@Bean
CommandLineRunner run(HealthStatusService healthStatusService)
```

возвращает объект типа:

```java
CommandLineRunner
```

Аннотация:

```java
@Bean
```

говорит Spring зарегистрировать возвращаемый объект как bean.

Параметр:

```java
HealthStatusService healthStatusService
```

Spring передаёт автоматически.

Поскольку `HealthStatusService` помечен:

```java
@Service
```

Spring уже создал его bean.

Получается:

```text
Spring
↓
создаёт HealthStatusService bean
↓
вызывает @Bean-метод run(...)
↓
передаёт HealthStatusService в параметр
↓
создаёт CommandLineRunner bean
```

Это ещё один вариант Dependency Injection.

Зависимость здесь передаётся не через конструктор класса, а через параметр `@Bean`-метода.

---

## Lambda expression

В коде:

```java
return args -> {
    String status = healthStatusService.getStatus();
    System.out.println(status);
};
```

конструкция:

```java
args -> {
    ...
}
```

является lambda expression.

`CommandLineRunner` имеет один основной абстрактный метод:

```java
void run(String... args)
```

Без lambda аналогичная идея потребовала бы более длинной реализации объекта.

Lambda позволяет компактно описать реализацию единственного абстрактного метода.

Упрощённо:

```text
CommandLineRunner
↓
метод run(args)
↓
lambda
↓
args -> { ... }
```

Здесь:

```text
args
→ параметр метода run(...)

->
→ отделяет параметры от выполняемого кода

{ ... }
→ тело метода
```

---

## Вывод в standard output

Внутри `CommandLineRunner`:

```java
String status = healthStatusService.getStatus();
```

создаётся локальная переменная:

```text
тип
→ String

имя
→ status
```

В неё записывается результат вызова метода:

```java
healthStatusService.getStatus()
```

Затем:

```java
System.out.println(status);
```

выводит строку в standard output.

При запуске программы в терминале было получено:

```text
UP
```

---

## Полная цепочка выполненной задачи

```text
./gradlew bootRun
↓
main()
↓
SpringApplication.run(...)
↓
Spring создаёт HealthStatusService
↓
HealthStatusService создаёт RestClient
↓
Spring запускает CommandLineRunner
↓
healthStatusService.getStatus()
↓
getHealthStatusResponse()
↓
RestClient
↓
HTTP GET
↓
Cloudbooking TEST API
↓
JSON response
↓
.body(HealthStatusResponse.class)
↓
HealthStatusResponse object
↓
response.getStatus()
↓
"UP"
↓
System.out.println(...)
↓
UP
```

Фактический результат в терминале:

```text
Started JavaSpringPlaygroundApplication
...
UP
```

То есть учебная задача выполнена полностью: программа на Spring Boot обращается к внешнему API, разбирает JSON-response и выводит значение поля `status` в standard output.