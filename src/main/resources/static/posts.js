const API_BASE = "http://localhost:8080/api/posts";

async function loadAllPosts() {
  try {
    const res = await fetch(API_BASE);
    const posts = await res.json();

    const container = document.getElementById("posts");
    container.innerHTML = "";

    if (posts.length === 0) {
      container.innerHTML = `<p class="text-gray-500">No blog posts available.</p>`;
      return;
    }

    posts.forEach((post) => {
      const div = document.createElement("div");
      div.className = "bg-white p-6 rounded-2xl shadow flex flex-col gap-2 relative overflow-hidden border border-gray-100";

      div.innerHTML = `
        <h2 class="text-xl font-semibold text-blue-800 mb-2">${post.title}</h2>
        <div class="prose max-w-none mb-3 post-content">${post.content}</div>
        <div class="text-sm text-gray-600 font-semibold mt-2 mb-1">${post.author ? `Author: ${post.author}` : ''}</div>
        <div class="flex flex-wrap justify-between items-end mt-2">
          <div>
            <span class="inline-block bg-gray-100 text-gray-700 text-xs px-3 py-1 rounded-full mr-2">Status: <strong>${post.status}</strong></span>
            <span class="text-xs text-gray-400">Created: ${formatDate(post.createdAt)}</span>
            <span class="text-xs text-gray-400 ml-2">Updated: ${formatDate(post.updatedAt)}</span>
          </div>
        </div>
      `;

      container.appendChild(div);
    });
  } catch (err) {
    console.error("Error loading posts:", err);
    document.getElementById("posts").innerHTML =
      "<p class='text-red-600'>Failed to load posts.</p>";
  }
}

function formatDate(datetime) {
  if (!datetime) return "N/A";
  const date = new Date(datetime);
  return date.toLocaleString("en-IN", {
    dateStyle: "medium",
    timeStyle: "short",
  });
}

window.onload = loadAllPosts;
