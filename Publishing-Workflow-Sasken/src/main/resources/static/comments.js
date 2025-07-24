const API_COMMENTS = "http://localhost:8080/api/collab/comments";

async function loadComments() {
  const res = await fetch(API_COMMENTS);
  const data = await res.json();

  const section = document.getElementById("comment-section");
  section.innerHTML = "";

  data.forEach(comment => {
    const div = document.createElement("div");
    div.className = "bg-white p-4 rounded shadow";
    div.innerHTML = `<p class="font-semibold">${comment.username}:</p><p>${comment.comment}</p>`;
    section.appendChild(div);
  });
}

async function addComment() {
  const comment = document.getElementById("new-comment").value.trim();
  const username = document.getElementById("comment-user").value.trim();

  if (!comment || !username) {
    alert("Please fill all fields!");
    return;
  }

  const res = await fetch(API_COMMENTS, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, comment })
  });

  if (res.ok) {
    alert("Comment added!");
    document.getElementById("new-comment").value = "";
    document.getElementById("comment-user").value = "";
    loadComments();
  } else {
    alert("Failed to add comment.");
  }
}

window.onload = loadComments;
