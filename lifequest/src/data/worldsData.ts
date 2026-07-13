export interface Room {
  id: string;
  name: string;
  damageToBoss: number;
  xpReward: number;
  goldReward: number;
  completed: boolean;
}

export interface Boss {
  name: string;
  maxHp: number;
  currentHp: number;
}

export interface World {
  id: string;
  name: string;
  description: string;
  boss: Boss;
  rooms: Room[];
}

export const initialWorlds: World[] = [
  {
    id: "world_1",
    name: "World 1: Java Fundamentals",
    description: "The beginning of the journey.",
    boss: {
      name: "OOP Master",
      maxHp: 500,
      currentHp: 500,
    },
    rooms: [
      { id: "w1_r1", name: "Variables & Data Types", damageToBoss: 100, xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r2", name: "Loops & Conditions", damageToBoss: 100, xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r3", name: "Functions & Methods", damageToBoss: 100, xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r4", name: "Classes & Objects", damageToBoss: 100, xpReward: 50, goldReward: 20, completed: false },
      { id: "w1_r5", name: "Inheritance & Polymorphism", damageToBoss: 100, xpReward: 100, goldReward: 50, completed: false },
    ],
  },
  {
    id: "world_2",
    name: "World 2: Database & SQL",
    description: "Learn how to store data permanently.",
    boss: {
      name: "Database Design",
      maxHp: 800,
      currentHp: 800,
    },
    rooms: [
      { id: "w2_r1", name: "SELECT, INSERT, UPDATE, DELETE", damageToBoss: 200, xpReward: 60, goldReward: 30, completed: false },
      { id: "w2_r2", name: "JOINs (INNER, LEFT, RIGHT)", damageToBoss: 200, xpReward: 80, goldReward: 40, completed: false },
      { id: "w2_r3", name: "Indexes & Performance", damageToBoss: 200, xpReward: 80, goldReward: 40, completed: false },
      { id: "w2_r4", name: "Database Normalization", damageToBoss: 200, xpReward: 100, goldReward: 50, completed: false },
    ],
  },
  {
    id: "world_3",
    name: "World 3: Spring Boot Core",
    description: "The heart of Java Backend.",
    boss: {
      name: "CRUD API",
      maxHp: 1000,
      currentHp: 1000,
    },
    rooms: [
      { id: "w3_r1", name: "Project Setup & IOC Container", damageToBoss: 200, xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r2", name: "Create API (POST)", damageToBoss: 200, xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r3", name: "Read API (GET)", damageToBoss: 200, xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r4", name: "Update & Delete API", damageToBoss: 200, xpReward: 80, goldReward: 40, completed: false },
      { id: "w3_r5", name: "Unit Testing", damageToBoss: 200, xpReward: 150, goldReward: 100, completed: false },
    ],
  }
];
