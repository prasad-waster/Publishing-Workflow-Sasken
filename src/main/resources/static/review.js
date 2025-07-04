const API_BASE = "http://localhost:8080/api/posts";

async function loadReviewPosts() {
  const res = await fetch(API_BASE);
  const posts = await res.json();
  const reviewPosts = posts.filter((p) => p.status === "REVIEW");

  const container = document.getElementById("review-posts");
  container.innerHTML = "";

  if (reviewPosts.length === 0) {
    container.innerHTML = "<p>No posts available for review.</p>";
    return;
  }

  reviewPosts.forEach((post) => {
    const div = document.createElement("div");
    div.className = "bg-white p-5 rounded shadow";
    div.innerHTML = `
      <h2 class="text-xl font-semibold text-blue-800">${post.title}</h2>
      <p class="mb-3">${post.content}</p>
      <button onclick="changeStatus(${post.id}, 'APPROVED')"
        class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded mr-2">
        ✅ Approve
      </button>
      <button onclick="changeStatus(${post.id}, 'DRAFT')"
        class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded">
        🔄 Revert to Draft
      </button>
    `;
    container.appendChild(div);
  });
}

async function changeStatus(postId, newStatus) {
  const reviewerId = 2;
  await fetch(
    `${API_BASE}/${postId}/status?status=${newStatus}&userId=${reviewerId}`,
    {
      method: "PUT",
    }
  );
  alert("Status changed!");
  loadReviewPosts();
}

window.onload = loadReviewPosts;
