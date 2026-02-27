<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <html>

        <head>
            <title>Courses</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                }

                table {
                    border-collapse: collapse;
                    width: 100%;
                }

                th,
                td {
                    border: 1px solid #ccc;
                    padding: 8px;
                    text-align: left;
                }

                th {
                    background: #27ae60;
                    color: white;
                }

                tr:nth-child(even) {
                    background: #f5f5f5;
                }

                .msg-error {
                    color: #c0392b;
                    font-weight: bold;
                    padding: 8px;
                    background: #fdecea;
                    border-radius: 4px;
                }

                .msg-success {
                    color: #27ae60;
                    font-weight: bold;
                    padding: 8px;
                    background: #eafaf1;
                    border-radius: 4px;
                }

                form.inline {
                    display: inline;
                }

                .form-row {
                    display: flex;
                    gap: 8px;
                    flex-wrap: wrap;
                    align-items: flex-end;
                    margin-bottom: 12px;
                }

                .form-row label {
                    display: block;
                    font-size: 12px;
                    color: #555;
                }

                .form-row input,
                .form-row select {
                    padding: 6px;
                    border: 1px solid #ccc;
                    border-radius: 4px;
                }

                button {
                    padding: 6px 14px;
                    background: #27ae60;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                }

                button:hover {
                    background: #1e8449;
                }

                nav a {
                    margin-right: 12px;
                    color: #4a6fa5;
                    text-decoration: none;
                    font-weight: bold;
                }
            </style>
        </head>

        <body>
            <nav>
                <a href="${pageContext.request.contextPath}/">🏠 Home</a>
                <a href="${pageContext.request.contextPath}/students">👤 Students</a>
                <a href="${pageContext.request.contextPath}/courses">📚 Courses</a>
            </nav>
            <hr />
            <h2>📚 Courses</h2>

            <c:if test="${not empty error}">
                <p class="msg-error">⚠️ [${statusCode}] ${error}</p>
            </c:if>
            <c:if test="${not empty success}">
                <p class="msg-success">✅ [${statusCode}] ${success}</p>
            </c:if>

            <%-- ── Add Course Form ── --%>
                <h3>Add New Course</h3>
                <form method="post" action="${pageContext.request.contextPath}/courses/create">
                    <div class="form-row">
                        <div><label>Course Code *</label><input name="courseCode" required placeholder="CS101"
                                style="width:80px" /></div>
                        <div><label>Course Name *</label><input name="courseName" required placeholder="Intro to CS"
                                style="width:200px" /></div>
                        <div><label>Credits</label><input name="credits" type="number" value="3" min="1" max="6"
                                style="width:50px" /></div>
                        <div><label>Instructor</label><input name="instructor" placeholder="Dr. Smith" /></div>
                        <div><label>Department</label><input name="department" placeholder="Computer Science" /></div>
                        <div><label>&nbsp;</label><button type="submit">Add Course</button></div>
                    </div>
                </form>

                <%-- ── Courses Table ── --%>
                    <table>
                        <tr>
                            <th>ID</th>
                            <th>Code</th>
                            <th>Course Name</th>
                            <th>Credits</th>
                            <th>Instructor</th>
                            <th>Department</th>
                            <th>Created</th>
                            <th>Actions</th>
                        </tr>
                        <c:forEach var="c" items="${courses}">
                            <tr>
                                <td>${c.courseId}</td>
                                <td><strong>${c.courseCode}</strong></td>
                                <td>${c.courseName}</td>
                                <td>${c.credits}</td>
                                <td>${c.instructor}</td>
                                <td>${c.department}</td>
                                <td>${c.createdAt}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/courses/edit?id=${c.courseId}">✏️
                                        Edit</a> &nbsp;
                                    <form class="inline" method="post"
                                        action="${pageContext.request.contextPath}/courses/delete"
                                        onsubmit="return confirm('Delete ${c.courseName}?')">
                                        <input type="hidden" name="id" value="${c.courseId}" />
                                        <button type="submit" style="background:#c0392b;">🗑 Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

        </body>

        </html>