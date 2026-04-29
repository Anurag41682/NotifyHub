package com.anurag.notifyhub.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
  public String home() {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <title>NotifyHub API</title>
            <style>
                body { font-family: -apple-system, sans-serif; max-width: 720px; margin: 60px auto; padding: 20px; line-height: 1.6; color: #1f2937; background: #f9fafb; }
                .card { background: white; padding: 32px; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
                h1 { color: #2563eb; margin-top: 0; }
                .badge { background: #10b981; color: white; padding: 2px 10px; border-radius: 12px; font-size: 12px; }
                .endpoint { background: #f3f4f6; padding: 10px 14px; border-radius: 6px; margin: 8px 0; font-family: 'SF Mono', Monaco, monospace; font-size: 13px; }
                .method { color: #2563eb; font-weight: 600; }
                a { color: #2563eb; text-decoration: none; }
                a:hover { text-decoration: underline; }
            </style>
        </head>
        <body>
            <div class="card">
                <h1>NotifyHub API <span class="badge">running</span></h1>
                <p>Async notification service with retry, DLQ, and Redis-based idempotency.</p>

                <h3>Endpoints</h3>
                <div class="endpoint"><span class="method">POST</span> /api/auth/register</div>
                <div class="endpoint"><span class="method">POST</span> /api/auth/login</div>
                <div class="endpoint"><span class="method">POST</span> /api/notifications <small>(JWT + Idempotency-Key)</small></div>
                <div class="endpoint"><span class="method">GET</span> /api/notifications/recipient/{id}</div>
                <div class="endpoint"><span class="method">GET</span> /api/notifications/{id}</div>
                <div class="endpoint"><span class="method">DELETE</span> /api/notifications/{id}</div>

                <h3>Tech Stack</h3>
                <p>Java • Spring Boot • RabbitMQ • Redis • MySQL • Docker • nginx</p>

                <p style="margin-top: 30px;">
                    <a href="https://github.com/Anurag41682/NotifyHub"> GitHub Repo & Docs →</a>
                </p>
            </div>
            <p style="text-align: center; color: #6b7280; font-size: 13px; margin-top: 20px;">Built by Anurag Yadav</p>
        </body>
        </html>
        """;
  }
}