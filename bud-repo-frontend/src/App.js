import { useState, useRef, useEffect } from "react";

const BACKEND = "http://localhost:8080";

export default function App() {
  const [repoUrl, setRepoUrl] = useState("");
  const [ingested, setIngested] = useState(false);
  const [ingestLoading, setIngestLoading] = useState(false);
  const [ingestStatus, setIngestStatus] = useState("");
  const [messages, setMessages] = useState([]);
  const [question, setQuestion] = useState("");
  const [chatLoading, setChatLoading] = useState(false);
  const bottomRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, chatLoading]);

  const handleIngest = async () => {
    if (!repoUrl.trim()) return;
    setIngestLoading(true);
    setIngestStatus("");
    try {
      const res = await fetch(`${BACKEND}/ingest?repoUrl=${encodeURIComponent(repoUrl)}`, {
        method: "POST",
      });
      const text = await res.text();
      setIngestStatus(text);
      setIngested(true);
      setMessages([]);
    } catch {
      setIngestStatus("Failed to connect to backend.");
    }
    setIngestLoading(false);
  };

  const handleChat = async () => {
    if (!question.trim() || chatLoading) return;
    const q = question.trim();
    setMessages((prev) => [...prev, { role: "user", text: q }]);
    setQuestion("");
    setChatLoading(true);
    try {
      const res = await fetch(`${BACKEND}/chat`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: q,
      });
      const answer = await res.text();
      setMessages((prev) => [...prev, { role: "ai", text: answer }]);
    } catch {
      setMessages((prev) => [...prev, { role: "ai", text: "Error getting response." }]);
    }
    setChatLoading(false);
  };

  return (
    <div style={s.page}>
      {/* Sidebar */}
      <div style={s.sidebar}>
        <div>
          <div style={s.logo}>⚡ BudRepo</div>
          <div style={s.tagline}>Codebase Intelligence</div>
        </div>

        <div style={s.ingestSection}>
          <div style={s.sectionLabel}>REPOSITORY</div>
          <input
            style={s.repoInput}
            placeholder="https://github.com/user/repo"
            value={repoUrl}
            onChange={(e) => setRepoUrl(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleIngest()}
          />
          <button
            style={{ ...s.ingestBtn, opacity: ingestLoading ? 0.6 : 1 }}
            onClick={handleIngest}
            disabled={ingestLoading}
          >
            {ingestLoading ? (
              <span>⏳ Indexing...</span>
            ) : (
              <span>🔍 Index Repo</span>
            )}
          </button>
          {ingestStatus && (
            <div style={s.ingestStatus}>
              ✅ {ingestStatus}
            </div>
          )}
        </div>

        <div style={s.sidebarFooter}>
          <div style={s.footerItem}>🧠 GPT-4o-mini</div>
          <div style={s.footerItem}>🔗 OpenAI Embeddings</div>
          <div style={s.footerItem}>☕ Java + Spring Boot</div>
        </div>
      </div>

      {/* Main chat area */}
      <div style={s.main}>
        {!ingested ? (
          <div style={s.emptyState}>
            <div style={s.emptyIcon}>🗂️</div>
            <div style={s.emptyTitle}>No repo indexed yet</div>
            <div style={s.emptySubtitle}>
              Paste a GitHub URL on the left and click Index Repo to get started.
            </div>
            <div style={s.exampleBox}>
              <div style={s.exampleLabel}>Try asking:</div>
              {["Where is authentication handled?", "How does the payment flow work?", "Which files should I edit to add a new API endpoint?"].map((ex) => (
                <div key={ex} style={s.exampleChip}>{ex}</div>
              ))}
            </div>
          </div>
        ) : (
          <div style={s.chatArea}>
            <div style={s.messages}>
              {messages.length === 0 && (
                <div style={s.chatHint}>Repo indexed! Ask anything about the codebase.</div>
              )}
              {messages.map((msg, i) => (
                <div key={i} style={msg.role === "user" ? s.userBubble : s.aiBubble}>
                  <div style={msg.role === "user" ? s.userLabel : s.aiLabel}>
                    {msg.role === "user" ? "You" : "⚡ BudRepo"}
                  </div>
                  <div style={s.msgText}>{msg.text}</div>
                </div>
              ))}
              {chatLoading && (
                <div style={s.aiBubble}>
                  <div style={s.aiLabel}>⚡ BudRepo</div>
                  <div style={s.typing}>
                    <span style={s.dot} />
                    <span style={{ ...s.dot, animationDelay: "0.2s" }} />
                    <span style={{ ...s.dot, animationDelay: "0.4s" }} />
                  </div>
                </div>
              )}
              <div ref={bottomRef} />
            </div>

            <div style={s.inputBar}>
              <input
                style={s.chatInput}
                placeholder="Ask about the codebase..."
                value={question}
                onChange={(e) => setQuestion(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && handleChat()}
              />
              <button
                style={{ ...s.sendBtn, opacity: chatLoading ? 0.5 : 1 }}
                onClick={handleChat}
                disabled={chatLoading}
              >
                ➤
              </button>
            </div>
          </div>
        )}
      </div>

      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap');
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { background: #0f0f0f; }
        @keyframes blink {
          0%, 80%, 100% { opacity: 0; }
          40% { opacity: 1; }
        }
      `}</style>
    </div>
  );
}

const s = {
  page: { display: "flex", height: "100vh", fontFamily: "'Inter', sans-serif", background: "#0f0f0f", color: "#f0f0f0" },

  // Sidebar
  sidebar: { width: 280, background: "#161616", borderRight: "1px solid #2a2a2a", display: "flex", flexDirection: "column", justifyContent: "space-between", padding: 24 },
  logo: { fontSize: 22, fontWeight: 600, color: "#fff", marginBottom: 4 },
  tagline: { fontSize: 12, color: "#555", marginBottom: 32, letterSpacing: 1 },
  ingestSection: { flex: 1 },
  sectionLabel: { fontSize: 10, color: "#444", letterSpacing: 2, marginBottom: 10, fontWeight: 600 },
  repoInput: { width: "100%", background: "#1e1e1e", border: "1px solid #2a2a2a", borderRadius: 8, padding: "10px 12px", color: "#f0f0f0", fontSize: 13, marginBottom: 10, outline: "none" },
  ingestBtn: { width: "100%", background: "#7c3aed", border: "none", borderRadius: 8, padding: "11px 0", color: "#fff", fontSize: 14, fontWeight: 500, cursor: "pointer", transition: "opacity 0.2s" },
  ingestStatus: { marginTop: 12, fontSize: 12, color: "#6ee7b7", lineHeight: 1.5 },
  sidebarFooter: { borderTop: "1px solid #2a2a2a", paddingTop: 16 },
  footerItem: { fontSize: 12, color: "#444", marginBottom: 6 },

  // Main
  main: { flex: 1, display: "flex", flexDirection: "column" },

  // Empty state
  emptyState: { flex: 1, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", padding: 40, textAlign: "center" },
  emptyIcon: { fontSize: 48, marginBottom: 16 },
  emptyTitle: { fontSize: 22, fontWeight: 600, marginBottom: 8 },
  emptySubtitle: { fontSize: 14, color: "#555", marginBottom: 32, maxWidth: 400, lineHeight: 1.6 },
  exampleBox: { background: "#161616", border: "1px solid #2a2a2a", borderRadius: 12, padding: 20, maxWidth: 420, width: "100%", textAlign: "left" },
  exampleLabel: { fontSize: 12, color: "#555", marginBottom: 12, letterSpacing: 1 },
  exampleChip: { background: "#1e1e1e", border: "1px solid #2a2a2a", borderRadius: 6, padding: "8px 12px", fontSize: 13, color: "#aaa", marginBottom: 8, cursor: "default" },

  // Chat
  chatArea: { flex: 1, display: "flex", flexDirection: "column" },
  messages: { flex: 1, overflowY: "auto", padding: "32px 40px", display: "flex", flexDirection: "column", gap: 16 },
  chatHint: { textAlign: "center", color: "#333", fontSize: 13, marginBottom: 8 },
  userBubble: { alignSelf: "flex-end", background: "#7c3aed", borderRadius: "16px 16px 4px 16px", padding: "12px 16px", maxWidth: "70%" },
  aiBubble: { alignSelf: "flex-start", background: "#1a1a1a", border: "1px solid #2a2a2a", borderRadius: "16px 16px 16px 4px", padding: "12px 16px", maxWidth: "80%" },
  userLabel: { fontSize: 11, color: "rgba(255,255,255,0.6)", marginBottom: 4, fontWeight: 500 },
  aiLabel: { fontSize: 11, color: "#7c3aed", marginBottom: 4, fontWeight: 500 },
  msgText: { fontSize: 14, lineHeight: 1.7, whiteSpace: "pre-wrap" },

  // Typing dots
  typing: { display: "flex", gap: 4, alignItems: "center", padding: "4px 0" },
  dot: { width: 6, height: 6, borderRadius: "50%", background: "#7c3aed", display: "inline-block", animation: "blink 1.2s infinite" },

  // Input bar
  inputBar: { padding: "16px 40px", borderTop: "1px solid #1e1e1e", display: "flex", gap: 12 },
  chatInput: { flex: 1, background: "#1a1a1a", border: "1px solid #2a2a2a", borderRadius: 10, padding: "12px 16px", color: "#f0f0f0", fontSize: 14, outline: "none" },
  sendBtn: { background: "#7c3aed", border: "none", borderRadius: 10, width: 48, color: "#fff", fontSize: 18, cursor: "pointer" },
};