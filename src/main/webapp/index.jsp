<%@ page contentType="text/html;charset=UTF-8" %>
    <html>

    <head>
        <title>MVC CRUD Home</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                padding: 24px;
            }

            .card {
                border: 1px solid #ddd;
                padding: 16px;
                border-radius: 10px;
                max-width: 820px;
            }

            .row {
                margin: 10px 0;
            }

            a,
            button {
                margin-right: 10px;
            }

            input {
                padding: 6px;
                margin-right: 8px;
            }

            .hint {
                color: #666;
                font-size: 13px;
                margin-top: 6px;
            }

            .section {
                margin-top: 18px;
                padding-top: 12px;
                border-top: 1px dashed #ddd;
            }

            .small {
                font-size: 12px;
                color: #666;
            }
        </style>
    </head>

    <body>

        <h2>JSP OK: <%= new java.util.Date() %>
        </h2>

        <div class="card">
            <h2>🏠 MVC CRUD Dashboard</h2>
            <div class="hint">
                Using <b>contextPath</b> so it works on any port and any context.
            </div>
            <div class="small">
                Context path detected: <b>
                    <%= request.getContextPath() %>
                </b>
            </div>

            <div class="section">
                <h3>1) Students (Single-page routes)</h3>

                <div class="row">
                    <!-- Always safe: list page -->
                    <a href="<%= request.getContextPath() %>/students">📄 View All Students</a>
                </div>

                <div class="row">
                    <!-- Create: POST /students/create -->
                    <form method="post" action="<%= request.getContextPath() %>/students/create"
                        style="display:inline;">
                        <input name="fullName" placeholder="Full Name" required />
                        <input name="email" placeholder="Email (optional)" />
                        <button type="submit">➕ Add Student</button>
                    </form>
                </div>

                <div class="row">
                    <!-- Edit: GET /students/edit?id= -->
                    <form method="get" action="<%= request.getContextPath() %>/students/edit" style="display:inline;">
                        <input name="id" placeholder="Student ID to Edit" required />
                        <button type="submit">✏️ Load Student for Edit</button>
                    </form>
                </div>

                <div class="row">
                    <!-- Update: POST /students/update -->
                    <form method="post" action="<%= request.getContextPath() %>/students/update"
                        style="display:inline;">
                        <input name="id" placeholder="Student ID" required />
                        <input name="fullName" placeholder="Updated Full Name" required />
                        <input name="email" placeholder="Updated Email (optional)" />
                        <button type="submit">✅ Update Student</button>
                    </form>
                </div>

                <div class="row">
                    <!-- Delete: POST /students/delete -->
                    <form method="post" action="<%= request.getContextPath() %>/students/delete" style="display:inline;"
                        onsubmit="return confirm('Delete this student?');">
                        <input name="id" placeholder="Student ID to Delete" required />
                        <button type="submit">🗑️ Delete Student</button>
                    </form>
                </div>
            </div>

            <div class="section">
                <h3>2) Quick Navigation</h3>
                <div class="row">
                    <a href="<%= request.getContextPath() %>/students">Go to /students</a>
                    <a href="<%= request.getContextPath() %>/">Reload Home</a>
                </div>
            </div>
        </div>

    </body>

    </html>