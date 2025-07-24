let allPosts = []; // store all posts for search

async function loadPublishedPosts() {
  try {
    const res = await fetch('/api/posts/published');
    const posts = await res.json();
    allPosts = posts; // Store for search

    renderPosts(posts);
  } catch (err) {
    console.error('Failed to load published posts:', err);
  }
}

async function loadDashboardStats() {
  try {
    const res = await fetch('/api/posts/dashboard/stats');
    const stats = await res.json();

    document.getElementById('totalBlogs').textContent = stats.totalPublishedBlogs;
    document.getElementById('totalViews').textContent = stats.totalViews;
    document.getElementById('totalLikes').textContent = stats.totalLikes;
    document.getElementById('totalComments').textContent = stats.totalComments;
    document.getElementById('mostViewedBlog').textContent = stats.mostViewedBlog || 'N/A';
    document.getElementById('mostLikedBlog').textContent = stats.mostLikedBlog || 'N/A';
    document.getElementById('newPostsThisWeek').textContent = stats.newPostsThisWeek;
    document.getElementById('lastPostDate').textContent = stats.lastPostDate || 'N/A';
  } catch (err) {
    console.error('Failed to load dashboard stats:', err);
  }
}

function renderPosts(posts) {
  const container = document.getElementById('postList');
  container.innerHTML = '';
  const noPosts = document.getElementById('noPosts');

  if (!posts.length) {
    noPosts.classList.remove('hidden');
    container.classList.add('hidden');
    return;
  } else {
    noPosts.classList.add('hidden');
    container.classList.remove('hidden');
  }

  posts.forEach(post => {
    const card = document.createElement('div');
    card.className = 'bg-white p-6 rounded-xl shadow-sm border card-hover space-y-3';

    const shouldShowReadMore = post.content.length > 5;
    const previewContent = shouldShowReadMore
      ? `${post.content.slice(0, 5)}... <button class="text-blue-500 underline" onclick="readMore(${post.id})">Read More</button>`
      : post.content;

    const postUrl = `http://localhost:8080/blog.html?id=${post.id}`;

    card.innerHTML = `
      <h2 class="text-xl font-semibold text-gray-900">${post.title}</h2>
      <p class="text-sm text-gray-500">${new Date(post.updatedAt).toLocaleDateString()}</p>

      <div class="text-gray-700" id="content-${post.id}">
        ${previewContent}
      </div>

      <p class="text-sm text-gray-500">Views: <span id="view-count-${post.id}">${post.views || 0}</span></p>

      <div class="flex items-center gap-4 mt-2 text-sm text-gray-600">
        <span>👍 <span id="like-count-${post.id}">${post.likes || 0}</span></span>
        <span>💬 <span id="comment-count-${post.id}">${post.comments?.length || 0}</span></span>
      </div>

      <div class="flex gap-2 mt-4">
        <button class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600" onclick="likePost(${post.id})">Like</button>
        <button class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600" onclick="toggleCommentBox(${post.id})">Comment</button>
        <button class="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600" onclick="editPost(${post.id})">Edit</button>
        <button class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600" onclick="deletePost(${post.id})">Delete</button>
      </div>

      <div class="mt-4 text-sm text-gray-700">
        🔗 <strong>Share:</strong>
        <a class="text-blue-600 underline" href="https://wa.me/?text=Check out this blog: ${postUrl}" target="_blank">WhatsApp</a> |
        <a class="text-blue-600 underline" href="https://www.facebook.com/sharer/sharer.php?u=${postUrl}" target="_blank">Facebook</a> |
        <a class="text-blue-600 underline" href="https://twitter.com/intent/tweet?url=${postUrl}&text=Awesome blog!" target="_blank">Twitter</a> |
        <a class="text-blue-600 underline" href="https://www.linkedin.com/shareArticle?mini=true&url=${postUrl}" target="_blank">LinkedIn</a> |
        <button class="text-blue-600 underline" onclick="copyToClipboard('${postUrl}')">Copy Link</button>
      </div>

      <div id="comment-box-${post.id}" class="mt-3 hidden">
        <input type="text" id="comment-input-${post.id}" placeholder="Write a comment..." class="border px-3 py-1 rounded w-full mb-2" onkeydown="handleCommentKey(event, ${post.id})" />

        <button class="bg-gray-800 text-white px-3 py-1 rounded hover:bg-gray-900" onclick="addComment(${post.id})">Submit Comment</button>
      </div>

      <div id="comments-${post.id}" class="text-sm text-gray-700 mt-2 space-y-1">
        ${(post.comments || []).map(comment => `<p>• ${comment}</p>`).join('')}
      </div>
    `;

    container.appendChild(card);
  });
}

function handleCommentKey(event, postId) {
  if (event.key === 'Enter') {
    event.preventDefault(); // Prevent default Enter behavior
    addComment(postId);     // Call your existing comment function
  }
}


function copyToClipboard(text) {
  navigator.clipboard.writeText(text).then(() => {
    alert("Link copied to clipboard!");
  }).catch(err => {
    console.error("Failed to copy link: ", err);
  });
}

async function readMore(postId) {
  try {
    const res = await fetch(`/api/posts/${postId}/view`, { method: 'PUT' });
    if (!res.ok) throw new Error('Failed to increment views');
    const updatedPost = await res.json();

    const contentDiv = document.getElementById(`content-${postId}`);
    const viewCountSpan = document.getElementById(`view-count-${postId}`);

    if (contentDiv) contentDiv.innerHTML = updatedPost.content;
    if (viewCountSpan) viewCountSpan.textContent = updatedPost.views;
    await loadDashboardStats();
  } catch (err) {
    console.error('Error loading full content or updating views:', err);
  }
}

async function likePost(postId) {
  try {
    const res = await fetch(`/api/posts/${postId}/like`, { method: 'POST' });
    if (res.ok) {
      const countSpan = document.getElementById(`like-count-${postId}`);
      countSpan.textContent = parseInt(countSpan.textContent) + 1;
      await loadDashboardStats();
    }
  } catch (err) {
    console.error('Error liking post:', err);
  }
}

function toggleCommentBox(postId) {
  const box = document.getElementById(`comment-box-${postId}`);
  if (box) {
    box.classList.toggle('hidden');
    if (!box.classList.contains('hidden')) {
      document.getElementById(`comment-input-${postId}`).focus();
    }
  }
}

async function addComment(postId) {
  const input = document.getElementById(`comment-input-${postId}`);
  const comment = input.value.trim();
  if (!comment) return;

  try {
    const res = await fetch(`/api/posts/${postId}/comment`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ comment })
    });

    if (res.ok) {
      const commentsDiv = document.getElementById(`comments-${postId}`);
      const commentCount = document.getElementById(`comment-count-${postId}`);
      commentsDiv.innerHTML += `<p>• ${comment}</p>`;
      commentCount.textContent = parseInt(commentCount.textContent) + 1;
      input.value = '';
      await loadDashboardStats();
    }
  } catch (err) {
    console.error('Error adding comment:', err);
  }
}

async function editPost(postId) {
  try {
    const res = await fetch(`/api/posts/${postId}`);
    if (!res.ok) throw new Error('Post not found');
    const post = await res.json();

    const newTitle = prompt('Edit Title:', post.title);
    if (newTitle === null) return;
    const newContent = prompt('Edit Content:', post.content);
    if (newContent === null) return;

    const updateRes = await fetch(`/api/posts/${postId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: postId,
        title: newTitle,
        content: newContent,
        status: post.status,
        authorId: post.authorId,
        createdAt: post.createdAt,
        updatedAt: post.updatedAt
      })
    });

    if (updateRes.ok) {
      loadPublishedPosts();
      loadDashboardStats(); // 🟢 Update dashboard after edit
    } else {
      console.error('Failed to update post:', await updateRes.text());
    }
  } catch (err) {
    console.error('Error editing post:', err);
  }
}

async function deletePost(postId) {
  if (!confirm("Are you sure you want to delete this post?")) return;
  try {
    const res = await fetch(`/api/posts/${postId}`, { method: 'DELETE' });
    if (res.ok) {
      loadPublishedPosts();
      loadDashboardStats(); // 🟢 Update dashboard after delete
    }
  } catch (err) {
    console.error('Error deleting post:', err);
  }
}

function filterPosts() {
  const query = document.getElementById('searchInput').value.toLowerCase();
  const filtered = allPosts.filter(post =>
    post.title.toLowerCase().includes(query) ||
    post.content.toLowerCase().includes(query)
  );
  renderPosts(filtered);
}

// Initial load
loadPublishedPosts();
loadDashboardStats();
