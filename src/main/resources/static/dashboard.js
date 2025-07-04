const API_BASE = "http://localhost:8080/api/posts";

// Simulated admin user (you can change to match your system)
const adminUserId = 3;

async function loadApprovedPosts() {
  try {
    const res = await fetch(API_BASE);
    const posts = await res.json();

    const approvedPosts = posts.filter((post) => post.status === "APPROVED");

    const container = document.getElementById("dashboard-posts");
    container.innerHTML = "";

    if (approvedPosts.length === 0) {
      container.innerHTML = `<p class="text-gray-500">No posts ready for publishing.</p>`;
      return;
    }

    approvedPosts.forEach((post) => {
      const div = document.createElement("div");
      div.className = "bg-white p-5 rounded shadow";

      div.innerHTML = `
        <h2 class="text-xl font-semibold text-blue-800 mb-2">${post.title}</h2>
        <p class="text-gray-700 mb-3">${post.content}</p>
        <div class="flex gap-4">
          <button onclick="changeStatus(${post.id}, 'PUBLISHED')"
            class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded">
            🚀 Publish
          </button>
          <button onclick="changeStatus(${post.id}, 'REVIEW')"
            class="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded">
            🔁 Back to Review
          </button>
        </div>
      `;

      container.appendChild(div);
    });
  } catch (err) {
    console.error("Error loading approved posts:", err);
    document.getElementById(
      "dashboard-posts"
    ).innerHTML = `<p class="text-red-600">Failed to load posts.</p>`;
  }
}

async function changeStatus(postId, newStatus) {
  try {
    const res = await fetch(
      `${API_BASE}/${postId}/status?status=${newStatus}&userId=${adminUserId}`,
      {
        method: "PUT",
      }
    );

    if (res.ok) {
      alert(
        `Post successfully ${
          newStatus === "PUBLISHED" ? "published" : "reverted to review"
        }.`
      );
      loadApprovedPosts();
    } else {
      const errText = await res.text();
      alert("Error updating status: " + errText);
    }
  } catch (err) {
    alert("Network error while updating status.");
    console.error(err);
  }
}

window.onload = loadApprovedPosts;
