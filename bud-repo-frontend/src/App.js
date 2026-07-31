import { useState } from "react";

const BACKEND = "http://localhost:8080";

export default function App() {
  const [repoUrl, setRepoUrl] = useState("");
  const [ingested, setIngested] = useState(false);
  const [ingestLoading, setIngestLoading] = useState(false);
  const [messages, setMessages] = useState([]);
  const [question, setQuestion] = useState("");
  const [chatLoading, setChatLoading] = useState(false);

  const handleIngest = async () => {
    setIngestLoading(true);
    const res = await fetch(`${BACKEND}/ingest?repoUrl=${encodeURIComponent(repoUrl)}`, {
      method: "POST",
    });
    const text = await res.text();
    setIngested(true);
    setIngestLoading(false);
    setMessages([{ role: "system", text }]);
  };

  const handleChat = async () => {
    if (!question.trim()) return;
    const userMsg = { role: "user", text: question };
    setMessages((prev) => [...prev, userMsg]);
    setQuestion("");
    setChatLoading(true);

    const res = await fetch(`${BACKEND}/chat`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: question,
    });
    const answer = await res.text();
    setMessages((prev) => [...prev, { role: "ai", text: answer }]);
    setChatLoading(false);
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>🤖 BudRepo</h1>
      <p style={styles.subtitle}>Ask questions about any GitHub codebase</p>

      <div style={styles.ingestBox}>
        <input
          style={styles.input}
          placeholder="https://github.com/user/repo"
          value={repoUrl}
          onChange={(e) => setRepoUrl(e.target.value)}
        />
        <button style={styles.button} onClick={handleIngest} disabled={ingestLoading}>
          {ingestLoading ? "Indexing..." : "Ingest Repo"}
        </button>
      </div>

      {messages.length > 0 && (
        <div style={styles.chatBox}>
          {messages.map((msg, i) => (
            <div key={i} style={msg.role === "user" ? styles.userMsg : styles.aiMsg}>
              <strong>{msg.role === "user" ? "You" : msg.role === "ai" ? "BudRepo" : "✅"}</strong>
              <p style={{ whiteSpace: "pre-wrap", margin: "4px 0 0 0" }}>{msg.text}</p>
            </div>
          ))}
          {chatLoading && <div style={styles.aiMsg}><em>Thinking...</em></div>}
        </div>
      )}

      {ingested && (
        <div style={styles.inputRow}>
          <input
            style={styles.input}
            placeholder="Where is authentication handled?"
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleChat()}
          />
          <button style={styles.button} onClick={handleChat} disabled={chatLoading}>
            Ask
          </button>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: { maxWidth: 720, margin: "60px auto", fontFamily: "sans-serif", padding: "0 20px" },
  title: { fontSize: 32, marginBottom: 4 },
  subtitle: { color: "#666", marginBottom: 24 },
  ingestBox: { display: "flex", gap: 8, marginBottom: 24 },
  input: { flex: 1, padding: "10px 14px", fontSize: 15, borderRadius: 8, border: "1px solid #ddd" },
  button: { padding: "10px 20px", fontSize: 15, borderRadius: 8, background: "#000", color: "#fff", border: "none", cursor: "pointer" },
  chatBox: { background: "#f9f9f9", borderRadius: 12, padding: 16, marginBottom: 16, maxHeight: 400, overflowY: "auto" },
  userMsg: { background: "#000", color: "#fff", borderRadius: 8, padding: "10px 14px", marginBottom: 10 },
  aiMsg: { background: "#fff", border: "1px solid #eee", borderRadius: 8, padding: "10px 14px", marginBottom: 10 },
  inputRow: { display: "flex", gap: 8 },
};