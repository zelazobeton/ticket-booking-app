# Architecture decision log

### 1. No tests
*Context and rationale*

The project is part of recrutation process in which its creator participate.
Project is temporary and will not be maintained. There is also little time to deliver it.

*Known effects*

Project will be difficult to maintain. Cooperation of multiple persons on the project will also be very difficult.
There is no regression to check if new functionalities or refactor do not break old ones.

### 2. Using JPA and Hibernate
*Context and rationale*

The scope of the project includes domain model with simple business logic. Performance of the system 
is not a priority as it has not been mentioned in the requirements. JPA with Hibernate allows for a fast
development and easy handling of associations and granularity. These are important factors as development 
time is an important factor. 

*Known effects*

Using ORMs such as Hibernate leads to worse performance than JdbcTemplate. ORM specification slightly constraints
definition of entity classes. It requires developers to have specific knowledge in order to understand when and 
what the ORM is doing. 

