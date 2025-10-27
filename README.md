
# Sistema Ave Fénix (Prototipo Java + MySQL)

Cumple consigna del módulo: POO (4 pilares), menú consola, estructuras (pila/cola/lista),
ordenamiento/búsqueda, excepciones y persistencia opcional con MySQL.

## Requisitos
- Java 17+
- Maven 3.8+
- (Opcional) MySQL 8.x

## Compilar y ejecutar
```bash
mvn -q -DskipTests package
java -cp target/SistemaAveFenix-1.0.0.jar avefenix.Main
```

## MySQL (opcional)
```sql
SOURCE sql/schema.sql;
```
```bash
export DB_URL="jdbc:mysql://localhost:3306/avefenix?useSSL=false&serverTimezone=UTC"
export DB_USER="tu_usuario"
export DB_PASS="tu_password"
```
