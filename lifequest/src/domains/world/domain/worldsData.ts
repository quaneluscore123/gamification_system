import { type WorldDef } from './models';

export const initialWorlds: WorldDef[] = [
  {
    id: "world_1",
    name: "World 1: Java Fundamentals",
    description: "The beginning of the journey.",
    requiredLevel: 1,
    boss: {
      id: "boss_1",
      name: "OOP Master",
      maxHp: 500,
    },
    rooms: [
      { id: "w1_r1", name: "Variables & Data Types", questId: "q_w1_r1", xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r2", name: "Loops & Conditions", questId: "q_w1_r2", xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r3", name: "Functions & Methods", questId: "q_w1_r3", xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r4", name: "Classes & Objects", questId: "q_w1_r4", xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r5", name: "Inheritance & Polymorphism", questId: "q_w1_r5", xpReward: 100, goldReward: 50, completed: false },
    ],
  },
  {
    id: "world_2",
    name: "World 2: Database & SQL",
    description: "Learn how to store data permanently.",
    requiredLevel: 5,
    boss: {
      id: "boss_2",
      name: "Database Design",
      maxHp: 800,
    },
    rooms: [
      { id: "w2_r1", name: "SELECT, INSERT, UPDATE, DELETE", questId: "q_w2_r1", xpReward: 60, goldReward: 30, completed: false },
      { id: "w2_r2", name: "JOINs (INNER, LEFT, RIGHT)", questId: "q_w2_r2", xpReward: 80, goldReward: 40, completed: false },
      { id: "w2_r3", name: "Indexes & Performance", questId: "q_w2_r3", xpReward: 80, goldReward: 40, completed: false },
      { id: "w2_r4", name: "Database Normalization", questId: "q_w2_r4", xpReward: 100, goldReward: 50, completed: false },
    ],
  },
  {
    id: "world_3",
    name: "World 3: Spring Boot Core",
    description: "The heart of Java Backend.",
    requiredLevel: 10,
    boss: {
      id: "boss_3",
      name: "CRUD API",
      maxHp: 1000,
    },
    rooms: [
      { id: "w3_r1", name: "Project Setup & IOC Container", questId: "q_w3_r1", xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r2", name: "Create API (POST)", questId: "q_w3_r2", xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r3", name: "Read API (GET)", questId: "q_w3_r3", xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r4", name: "Update & Delete API", questId: "q_w3_r4", xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r5", name: "Unit Testing", questId: "q_w3_r5", xpReward: 150, goldReward: 100, completed: false },
    ],
  }
];
