import { type QuestDef } from './models';

export const dailyQuests: QuestDef[] = [
  {
    id: "dq_1",
    title: "Write 50 lines of code",
    type: "DAILY",
    difficulty: "EASY",
    estimatedTimeMin: 30,
    tags: ["coding", "habit"],
    rewards: { xp: 50, gold: 10, bossDamage: 10 }
  },
  {
    id: "dq_2",
    title: "Read 1 technical article",
    type: "DAILY",
    difficulty: "EASY",
    estimatedTimeMin: 15,
    tags: ["learning"],
    rewards: { xp: 30, gold: 5, bossDamage: 0 }
  },
  {
    id: "dq_3",
    title: "Review Flashcards / Concepts",
    type: "DAILY",
    difficulty: "MEDIUM",
    estimatedTimeMin: 20,
    tags: ["memory"],
    rewards: { xp: 50, gold: 15, bossDamage: 20 }
  }
];

export const weeklyQuests: QuestDef[] = [
  {
    id: "wq_1",
    title: "Complete a Mini-Project",
    type: "WEEKLY",
    difficulty: "HARD",
    estimatedTimeMin: 180,
    tags: ["project", "milestone"],
    rewards: { xp: 300, gold: 100, bossDamage: 150 }
  },
  {
    id: "wq_2",
    title: "Learn a new Framework/Library",
    type: "WEEKLY",
    difficulty: "HARD",
    estimatedTimeMin: 120,
    tags: ["learning"],
    rewards: { xp: 200, gold: 80, bossDamage: 100 }
  }
];

// Map Main Quests to World Rooms
export const mainQuests: QuestDef[] = [
  // World 1
  { id: "q_w1_r1", title: "Variables & Data Types", type: "MAIN", difficulty: "EASY", estimatedTimeMin: 60, tags: ["java", "core"], rewards: { xp: 50, gold: 20, bossDamage: 100 } },
  { id: "q_w1_r2", title: "Loops & Conditions", type: "MAIN", difficulty: "EASY", estimatedTimeMin: 60, tags: ["java", "core"], rewards: { xp: 50, gold: 20, bossDamage: 100 } },
  { id: "q_w1_r3", title: "Functions & Methods", type: "MAIN", difficulty: "MEDIUM", estimatedTimeMin: 90, tags: ["java", "core"], rewards: { xp: 50, gold: 20, bossDamage: 100 } },
  { id: "q_w1_r4", title: "Classes & Objects", type: "MAIN", difficulty: "MEDIUM", estimatedTimeMin: 120, tags: ["java", "oop"], rewards: { xp: 50, gold: 20, bossDamage: 100 } },
  { id: "q_w1_r5", title: "Inheritance & Polymorphism", type: "MAIN", difficulty: "HARD", estimatedTimeMin: 150, tags: ["java", "oop"], rewards: { xp: 100, gold: 50, bossDamage: 100 } },
  
  // World 2
  { id: "q_w2_r1", title: "SELECT, INSERT, UPDATE, DELETE", type: "MAIN", difficulty: "EASY", estimatedTimeMin: 60, tags: ["sql"], rewards: { xp: 60, gold: 30, bossDamage: 200 } },
  { id: "q_w2_r2", title: "JOINs (INNER, LEFT, RIGHT)", type: "MAIN", difficulty: "MEDIUM", estimatedTimeMin: 90, tags: ["sql"], rewards: { xp: 80, gold: 40, bossDamage: 200 } },
  { id: "q_w2_r3", title: "Indexes & Performance", type: "MAIN", difficulty: "HARD", estimatedTimeMin: 120, tags: ["sql"], rewards: { xp: 80, gold: 40, bossDamage: 200 } },
  { id: "q_w2_r4", title: "Database Normalization", type: "MAIN", difficulty: "HARD", estimatedTimeMin: 180, tags: ["sql", "design"], rewards: { xp: 100, gold: 50, bossDamage: 200 } },
  
  // World 3
  { id: "q_w3_r1", title: "Project Setup & IOC Container", type: "MAIN", difficulty: "MEDIUM", estimatedTimeMin: 120, tags: ["spring"], rewards: { xp: 80, gold: 40, bossDamage: 200 } },
  { id: "q_w3_r2", title: "Create API (POST)", type: "MAIN", difficulty: "MEDIUM", estimatedTimeMin: 90, tags: ["spring", "api"], rewards: { xp: 80, gold: 40, bossDamage: 200 } },
  { id: "q_w3_r3", title: "Read API (GET)", type: "MAIN", difficulty: "MEDIUM", estimatedTimeMin: 90, tags: ["spring", "api"], rewards: { xp: 80, gold: 40, bossDamage: 200 } },
  { id: "q_w3_r4", title: "Update & Delete API", type: "MAIN", difficulty: "MEDIUM", estimatedTimeMin: 90, tags: ["spring", "api"], rewards: { xp: 80, gold: 40, bossDamage: 200 } },
  { id: "q_w3_r5", title: "Unit Testing", type: "MAIN", difficulty: "HARD", estimatedTimeMin: 150, tags: ["spring", "test"], rewards: { xp: 150, gold: 100, bossDamage: 200 } }
];

export const allQuests = [...dailyQuests, ...weeklyQuests, ...mainQuests];
