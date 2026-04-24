# BudRepo 🤖

An AI-powered codebase onboarding companion. Point it at any GitHub repo and ask questions about the code in plain English.

## The Problem
Joining a new codebase takes weeks. There's no interactive guide to "how does this repo actually work."

## What It Does
- Clones any public GitHub repository
- Reads and chunks all Java source files
- Generates embeddings for each chunk using OpenAI
- On each question, finds the most relevant code via cosine similarity
- Answers in plain English using GPT-4o-mini

## How It Works (RAG Pipeline)
GitHub Repo → JGit Clone → File Chunking → OpenAI Embeddings
→ Cosine Similarity Search → GPT-4o-mini → Answer


## Tech Stack
- Java + Spring Boot
- JGit (repo cloning)
- OpenAI Embeddings API (text-embedding-3-small)
- OpenAI Chat API (gpt-4o-mini)
- Jackson (JSON parsing)

## Example
Ask: Where is user authentication handled?

Answer: Authentication is handled across SecurityConfig.java,
JwtRequestFilter.java, and JwtUtil.java...


## Setup
1. Clone this repo
2. Set your OpenAI API key: `export OPENAI_API_KEY=your_key`
3. Run `BudRepoApplication.java`