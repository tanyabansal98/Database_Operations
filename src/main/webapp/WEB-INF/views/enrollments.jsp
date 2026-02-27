<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <html>

        <head>
            <title>Enrollments – ${student.full_Name}</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                }

                table {
                    border-collapse: collapse;
                    width: 100%;
                    margin-top: 10px;
                }

                th,
                td {
                    border: 1px solid #ccc;
                    padding: 8px;
                    text-align: left;
                }

                th {
                    background: #8e44ad;
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
                    align-items: flex-end;
                    margin-bottom: 12px;
                }

                .form-row label {
                    display: block;
                    font-size: 12px;
                    color: #555;
                }

                .form-row select {
                    padding: 6px;
                    border: 1px solid #ccc;
                    border-radius: 4px;
                    min-width: 220px;
                }

                button {
                    padding: 6px 14px;
                    background: #8e44ad;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                }

                button:hover {
                    background: #71368a;
                }

                nav a {
                    margin-right: 12px;
                    color: #4a6fa5;
                    text-decoration: none;
                    font-weight: bold;
                }

                .student-card {
                    background: #f8f4fc;
                    border: 1px solid #d7bde2;
                    border-radius: 6px;
                    padding: 12px;
                    margin-bottom: 16px;
                }

                .empty {
                    color: #888;
                    font-style: italic;
                    padding: 10px;
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

            <%-- ── Student Info Card ── --%>
                <div class="student-card">
                    <h2>📋 Enrollments for: ${student.full_Name}</h2>
                    <p>Major: <strong>${student.major}</strong> &nbsp;|&nbsp;
                        Status: <strong>${student.status}</strong> &nbsp;|&nbsp;
                        Email: ${student.email}</p>
                </div>

                <c:if test="${not empty error}">
                    <p class="msg-error">⚠️ [${statusCode}] ${error}</p>
                </c:if>
                <c:if test="${not empty success}">
                    <p class="msg-success">✅ [${statusCode}] ${success}</p>
                </c:if>

                <%-- ── Enroll in a Course Form ── --%>
                    <h3>Enroll in a Course</h3>
                    <form method="post" action="${pageContext.request.contextPath}/enrollments/enroll">
                        <input type="hidden" name="studentId" value="${student.student_Id}" />
                        <div class="form-row">
                            <div>
                                <label>Select Course</label>
                                <select name="courseId" required>
                                    <option value="">-- Choose a course --</option>
                                    <c:forEach var="course" items="${allCourses}">
                                        <option value="${course.courseId}">
                                            ${course.courseCode} – ${course.courseName} (${course.credits} cr)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div><label>&nbsp;</label><button type="submit">Enroll</button></div>
                        </div>
                    </form>

                    <%-- ── Current Enrollments Table ── --%>
                        <h3>Current Enrollments</h3>
                        <c:choose>
                            <c:when test="${empty enrollments}">
                                <p class="empty">No courses enrolled yet.</p>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <tr>
                                        <th>Course Code</th>
                                        <th>Course Name</th>
                                        <th>Grade</th>
                                        <th>Enrolled On</th>
                                        <th>Action</th>
                                    </tr>
                                    <c:forEach var="e" items="${enrollments}">
                                        <tr>
                                            <td><strong>${e.courseCode}</strong></td>
                                            <td>${e.courseName}</td>
                                            <td>${not empty e.grade ? e.grade : '—'}</td>
                                            <td>${e.enrolledAt}</td>
                                            <td>
                                                <form class="inline" method="post"
                                                    action="${pageContext.request.contextPath}/enrollments/drop"
                                                    onsubmit="return confirm('Drop ${e.courseName}?')">
                                                    <input type="hidden" name="studentId"
                                                        value="${student.student_Id}" />
                                                    <input type="hidden" name="courseId" value="${e.courseId}" />
                                                    <button type="submit" style="background:#c0392b;">🗑 Drop</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:otherwise>
                        </c:choose>

                        <br />
                        <a href="${pageContext.request.contextPath}/students">← Back to Students</a>
        </body>

        </html>