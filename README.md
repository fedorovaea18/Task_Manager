### Hexlet tests and linter status:
[![Actions Status](https://github.com/fedorovaea18/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/fedorovaea18/java-project-99/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/8f66645940527b6973cd/maintainability)](https://codeclimate.com/github/fedorovaea18/java-project-99/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/8f66645940527b6973cd/test_coverage)](https://codeclimate.com/github/fedorovaea18/java-project-99/test_coverage)
[![GitHub Actions Status](https://github.com/fedorovaea18/java-project-99/actions/workflows/github-actions.yml/badge.svg)](https://github.com/fedorovaea18/java-project-99/actions)

# **Task Manager**
This project is a system for task managing like [Redmine](http://www.redmine.org). You can set tasks, assign performers and change their statuses. Registration and authentication are required. The application can be used both locally and in a production environment.

## Local start

If you want to start this project locally, enter this command in terminal:

```
git clone git@github.com:fedorovaea18/java-project-99.git
cd java-project-99
make run-dev
```
Then open http://localhost:8080 on your browser and enter admin details:

```
Username: hexlet@example.com
Password: qwerty
```
### **Technology stack:**
- _Framework: Spring Boot_;
- _Authentication: Spring Security_;
- _Database: H2(for local development), PostgreSQL(for production)_;
- _Testing: JUnit 5, MockWebServer, Datafaker_;
- _API Documentation: Swagger_;
- _Monitoring and error tracking: Sentry_; 
- _Deploy: https://render.com/ (model PaaS)_.

### **Deploy on Render.com:** https://java-project-99-yx3q.onrender.com
### **Interactive documentation:** https://java-project-99-yx3q.onrender.com/swagger-ui/index.html
