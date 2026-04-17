<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen" />
  <img src="https://img.shields.io/badge/Java-17%2B-blue" />
  <img src="https://img.shields.io/badge/license-MIT-orange" />
  <img src="https://img.shields.io/badge/status-active-success" />
</p>

<h1 align="center">🚨 Telegram Error Notifier</h1>

<p align="center">
A powerful <b>Spring Boot starter</b> that automatically sends detailed exception reports to Telegram in real-time.
</p>

---

<p align="center">
  <img src="https://media.giphy.com/media/3o7aCTfyhYawdOXcFW/giphy.gif" width="500"/>
</p>

---

# ⚡ Why this library?

✔ Zero configuration (plug & play)
✔ Automatic global exception capture
✔ Full HTTP request context
✔ Async Telegram alerts
✔ Production-ready logging style

---

# 🚀 Installation

```xml id="install_001"
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/kuanov007/telegram-error-notifier</url>
  </repository>
</repositories>

<dependency>
  <groupId>io.github.azamat</groupId>
  <artifactId>telegram-error-notifier</artifactId>
  <version>1.0.0</version>
</dependency>
```

---

# ⚙️ Configuration

```yaml id="config_001"
telegram:
  bot:
    token: YOUR_BOT_TOKEN
    chat-id: YOUR_CHAT_ID
    enabled: true
```

---

# 🔥 What you get

### Automatically sends this to Telegram:

```
🚨 [MyApp] XATOLIK ANIQLANDI

🕒 Vaqt: ...
🔢 Status: 500
⚠️ Exception: NullPointerException
📩 Message: Something went wrong

🌐 Endpoint: POST /api/v1/orders
📍 IP: 127.0.0.1
👤 User: admin

📦 Body:
{...}
```

---

# 🧠 Architecture

```
Request → Filter → Exception Handler → Builder → Telegram Service → Bot API
```

---

# 🛑 Disable anytime

```yaml id="disable_001"
telegram:
  bot:
    enabled: false
```

---

# 🎯 Use Cases

* Microservices monitoring
* Production debugging
* DevOps alerting
* Critical system logging

---

# 📦 Tech Stack

* Spring Boot 3
* Java 17+
* WebClient
* Telegram Bot API

---

# 👨‍💻 Author

**Azamat**
Java Backend Developer

---

# ⭐ Support

If you like this project:

* ⭐ Star the repo
* 🍴 Fork it
* 🐛 Report issues
* 🚀 Contribute

---

<p align="center">
Made with ❤️ for better backend debugging
</p>
