const API_BASE = "http://localhost:8080/api/collab/drafts"; // ✅ Using collabtool endpoint

// Load all drafts on page load
async function loadDrafts() {
  try {
    const response = await fetch(API_BASE);
    const drafts = await response.json();

    const container = document.getElementById("draft-posts");
    container.innerHTML = "";

    if (drafts.length === 0) {
      container.innerHTML = "<p class='text-gray-500'>No drafts available.</p>";
      return;
    }

    drafts.forEach((draft) => {
      const draftCard = document.createElement("div");
      draftCard.className = "bg-white p-5 rounded shadow";

      draftCard.innerHTML = `
        <h2 class="text-xl font-bold mb-1">${draft.title}</h2>
        <p class="text-gray-600 mb-2"><strong>Author:</strong> ${draft.username}</p>
        <p class="mb-4">${draft.content}</p>

        <div class="flex flex-wrap gap-3">
          <button onclick="editDraft(${draft.id})" class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600">
            ✏️ Edit
          </button>
          <button onclick="deleteDraft(${draft.id})" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
            🗑️ Delete
          </button>
          <button onclick="viewComments(${draft.id})" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
            💬 Collaborate
          </button>
        </div>
      `;

      container.appendChild(draftCard);
    });
  } catch (error) {
    console.error("❌ Failed to load drafts:", error);
    alert("Unable to fetch drafts from server.");
  }
}

// Edit draft
function editDraft(draftId) {
  window.location.href = `edit.html?id=${draftId}`;
}

// View collaboration/comments
function viewComments(draftId) {
  window.location.href = `comments.html?draftId=${draftId}`;
}

// Delete draft
async function deleteDraft(draftId) {
  const confirmDelete = confirm("Are you sure you want to delete this draft?");
  if (!confirmDelete) return;

  try {
    const response = await fetch(`${API_BASE}/${draftId}`, { method: "DELETE" });
    if (response.ok) {
      alert("✅ Draft deleted.");
      loadDrafts();
    } else {
      alert("❌ Failed to delete draft.");
    }
  } catch (error) {
    console.error("Error deleting draft:", error);
    alert("Server error while deleting draft.");
  }
}

// Load drafts on window load
window.onload = loadDrafts;
