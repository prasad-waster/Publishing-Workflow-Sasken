const API_BASE = "http://localhost:8080/api/posts";
const authorId = 1; // Simulated author

async function loadDrafts() {
  try {
    const res = await fetch(API_BASE);
    const posts = await res.json();

    const drafts = posts.filter((p) => p.status === "DRAFT");

    const container = document.getElementById("draft-posts");
    container.innerHTML = "";

    if (drafts.length === 0) {
      container.innerHTML = "<p class='text-gray-500'>No drafts available.</p>";
      return;
    }

    drafts.forEach((post) => {
      const div = document.createElement("div");
      div.className = "bg-white p-6 rounded-2xl shadow flex flex-col gap-2 relative overflow-hidden border border-gray-100";

      div.innerHTML = `
        <h2 class="text-xl font-semibold text-blue-800 mb-2">${post.title}</h2>
        <div class="prose max-w-none mb-3 post-content">${post.content}</div>
        <div class="text-sm text-gray-600 font-semibold mt-2 mb-1">${post.author ? `Author: ${post.author}` : ''}</div>
        <div class="flex gap-4 mt-2">
          <button onclick="editDraft(${post.id})" class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600">✏️ Edit</button>
          <button onclick="deleteDraft(${post.id})" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">🗑️ Delete</button>
        </div>
      `;

      container.appendChild(div);
    });
  } catch (err) {
    console.error("Failed to load drafts:", err);
  }
}

function editDraft(postId) {
  window.location.href = `edit.html?id=${postId}`;
}

async function deleteDraft(postId) {
  if (!confirm("Are you sure you want to delete this draft?")) return;

  const res = await fetch(`${API_BASE}/${postId}`, { method: "DELETE" });
  if (res.ok) {
    alert("Draft deleted.");
    loadDrafts();
  } else {
    alert("Failed to delete draft.");
  }
}

window.onload = loadDrafts;
