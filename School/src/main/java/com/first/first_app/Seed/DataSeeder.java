// package com.first.first_app.Seed;

// import java.util.*;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// import com.first.first_app.Model.*;
// import com.first.first_app.Repo.*;

// @Component
// public class DataSeeder implements CommandLineRunner {

//     private final LevelRepo levelRepo;
//     private final SubjectRepo subjectRepo;
//     private final TeacherRepo teacherRepo;
//     private final StudentRepo studentRepo;
//     private final ParentRepo parentRepo;
//     private final AssessmentRepo assessmentRepo;
//     private final QuestionRepo questionRepo;
//     private final ScoreRepo scoreRepo;

//     @Value("${seed.force:false}")
//     private boolean seedForce;

//     public DataSeeder(LevelRepo levelRepo,
//                       SubjectRepo subjectRepo,
//                       TeacherRepo teacherRepo,
//                       StudentRepo studentRepo,
//                       ParentRepo parentRepo,
//                       AssessmentRepo assessmentRepo,
//                       QuestionRepo questionRepo,
//                       ScoreRepo scoreRepo) {
//         this.levelRepo       = levelRepo;
//         this.subjectRepo     = subjectRepo;
//         this.teacherRepo     = teacherRepo;
//         this.studentRepo     = studentRepo;
//         this.parentRepo      = parentRepo;
//         this.assessmentRepo  = assessmentRepo;
//         this.questionRepo    = questionRepo;
//         this.scoreRepo       = scoreRepo;
//     }

//     // =========================================================================
//     // REAL QUESTION BANKS  (question, A, B, C, D, correctEnum)
//     // Each bank has 32 entries so EXAM (30 Qs) never repeats.
//     // =========================================================================

//     private static final List<String[]> MATH_BANK = List.of(
//         new String[]{"15 + 27 = ?",                          "32","40","42","45",                          "choiceC"},
//         new String[]{"144 ÷ 12 = ?",                         "10","11","12","13",                          "choiceC"},
//         new String[]{"√81 = ?",                              "7","8","9","10",                             "choiceC"},
//         new String[]{"3x = 18, x = ?",                       "3","6","9","12",                             "choiceB"},
//         new String[]{"25% of 200 = ?",                       "25","40","50","60",                          "choiceC"},
//         new String[]{"2³ = ?",                               "4","6","8","12",                             "choiceC"},
//         new String[]{"52 − 18 = ?",                          "24","34","44","54",                          "choiceB"},
//         new String[]{"7 × 8 = ?",                            "48","54","56","64",                          "choiceC"},
//         new String[]{"LCM of 4 and 6?",                      "8","10","12","24",                           "choiceC"},
//         new String[]{"GCF of 12 and 18?",                    "3","6","9","12",                             "choiceB"},
//         new String[]{"0.5 × 0.5 = ?",                        "0.025","0.05","0.25","0.5",                  "choiceC"},
//         new String[]{"Area of rectangle 5×8?",               "13","20","35","40",                          "choiceD"},
//         new String[]{"Perimeter of square side 7?",          "14","21","28","35",                          "choiceC"},
//         new String[]{"60% of 150 = ?",                       "60","80","90","100",                         "choiceC"},
//         new String[]{"(-3) × (-4) = ?",                      "-12","-7","7","12",                          "choiceD"},
//         new String[]{"√144 = ?",                             "10","11","12","13",                          "choiceC"},
//         new String[]{"5! = ?",                               "60","100","120","150",                       "choiceC"},
//         new String[]{"Slope of y=3x+2?",                     "2","3","5","6",                              "choiceB"},
//         new String[]{"2x + 4 = 10, x = ?",                   "2","3","4","5",                              "choiceB"},
//         new String[]{"Area of circle r=7 (pi~22/7)?",        "44","77","154","308",                        "choiceC"},
//         new String[]{"Sum of angles in a triangle?",         "90","120","180","360",                       "choiceC"},
//         new String[]{"1/2 + 1/3 = ?",                        "2/5","2/6","5/6","3/5",                      "choiceC"},
//         new String[]{"3/4 × 8 = ?",                          "3","4","6","8",                              "choiceC"},
//         new String[]{"(2+3)^2 = ?",                          "10","20","25","30",                          "choiceC"},
//         new String[]{"12^2 = ?",                             "124","134","144","154",                      "choiceC"},
//         new String[]{"Median of {3,7,9,11,13}?",             "7","9","10","11",                            "choiceB"},
//         new String[]{"Mean of {4,8,12,16}?",                 "8","10","12","14",                           "choiceB"},
//         new String[]{"Hypotenuse of 3-4-? right triangle?",  "4","5","6","7",                              "choiceB"},
//         new String[]{"Ratio 3:5, total 40, smaller part?",   "12","15","18","24",                          "choiceB"},
//         new String[]{"3/5 as percentage?",                   "35%","60%","65%","75%",                      "choiceB"},
//         new String[]{"(-5) + 8 = ?",                         "-3","2","3","13",                            "choiceC"},
//         new String[]{"Value of pi approx?",                  "2.14","3.00","3.14","3.41",                  "choiceC"}
//     );

//     private static final List<String[]> SCIENCE_BANK = List.of(
//         new String[]{"Main source of energy for Earth?",             "Moon","Sun","Stars","Wind",                            "choiceB"},
//         new String[]{"Plants absorb which gas for photosynthesis?",  "Oxygen","CO2","Nitrogen","Helium",                    "choiceB"},
//         new String[]{"Water boils at?",                              "50C","90C","100C","120C",                             "choiceC"},
//         new String[]{"Heart main function?",                         "Breathing","Pumping blood","Digestion","Movement",    "choiceB"},
//         new String[]{"Unit of force?",                               "Joule","Watt","Newton","Pascal",                      "choiceC"},
//         new String[]{"Chemical symbol for Gold?",                    "Go","Gd","Au","Ag",                                   "choiceC"},
//         new String[]{"Human body has how many bones?",               "186","196","206","216",                               "choiceC"},
//         new String[]{"Photosynthesis produces?",                     "CO2","Glucose","Nitrogen","Water only",               "choiceB"},
//         new String[]{"Gravity acceleration on Earth approx?",        "8 m/s2","9.8 m/s2","10.8 m/s2","12 m/s2",           "choiceB"},
//         new String[]{"Smallest particle of an element?",             "Cell","Atom","Molecule","Electron",                   "choiceB"},
//         new String[]{"Water chemical formula?",                      "HO","H2O","H2O2","HO2",                              "choiceB"},
//         new String[]{"Solid to Gas directly is called?",             "Melting","Evaporation","Sublimation","Condensation",  "choiceC"},
//         new String[]{"Largest planet in solar system?",              "Saturn","Uranus","Neptune","Jupiter",                 "choiceD"},
//         new String[]{"Organisms that make own food?",                "Herbivores","Carnivores","Autotrophs","Decomposers",  "choiceC"},
//         new String[]{"DNA carries?",                                 "Energy","Nutrients","Genetic info","Hormones",         "choiceC"},
//         new String[]{"Newton's 1st Law is about?",                   "Gravity","Acceleration","Inertia","Action-Reaction",  "choiceC"},
//         new String[]{"Universal blood donor type?",                  "A","B","AB","O",                                      "choiceD"},
//         new String[]{"Ohm's Law: V = ?",                             "I+R","I-R","I x R","I/R",                            "choiceC"},
//         new String[]{"Sound travels fastest in?",                    "Air","Water","Vacuum","Steel",                        "choiceD"},
//         new String[]{"Mitochondria is known as?",                    "Brain of cell","Powerhouse of cell","Wall","Storage", "choiceB"},
//         new String[]{"Ozone layer protects from?",                   "Gravity","UV radiation","Acid rain","Wind",           "choiceB"},
//         new String[]{"Metals are generally?",                        "Brittle","Insulators","Conductors","Non-malleable",   "choiceC"},
//         new String[]{"Ecosystem includes?",                          "Only animals","Only plants","Living + non-living","Only soil","choiceC"},
//         new String[]{"Acid turns litmus paper?",                     "Blue","Green","Red","Yellow",                         "choiceC"},
//         new String[]{"Moon is a?",                                   "Star","Planet","Natural satellite","Comet",           "choiceC"},
//         new String[]{"Bone connected to muscle by?",                 "Ligament","Cartilage","Tendon","Nerve",               "choiceC"},
//         new String[]{"Work = Force x ?",                             "Mass","Time","Velocity","Distance",                   "choiceD"},
//         new String[]{"Evaporation happens at?",                      "0C only","Any temperature","100C only","-10C",        "choiceB"},
//         new String[]{"Cell wall is found in?",                       "Animal cells only","Both","Plant cells only","Fungi only","choiceC"},
//         new String[]{"Friction acts?",                               "In direction of motion","Opposite to motion","Perpendicular","No direction","choiceB"},
//         new String[]{"Lungs exchange?",                              "Food","Oxygen and CO2","Blood and water","Nutrients",  "choiceB"},
//         new String[]{"Speed of light approx?",                       "3x10^5 km/s","3x10^6 km/s","3x10^7 km/s","3x10^8 km/s","choiceA"}
//     );

//     private static final List<String[]> ENGLISH_BANK = List.of(
//         new String[]{"She ___ to school daily.",               "go","goes","gone","going",                        "choiceB"},
//         new String[]{"Opposite of strong?",                    "Weak","Tall","Fast","Hard",                       "choiceA"},
//         new String[]{"Plural of mouse?",                       "mouses","mice","mouse","mices",                   "choiceB"},
//         new String[]{"Past tense of run?",                     "runned","runs","ran","running",                   "choiceC"},
//         new String[]{"Correct article: ___ apple.",            "a","an","the","no article",                       "choiceB"},
//         new String[]{"Synonym of happy?",                      "Sad","Glad","Angry","Tired",                      "choiceB"},
//         new String[]{"They ___ playing now.",                  "is","was","are","were",                           "choiceC"},
//         new String[]{"Opposite of expensive?",                 "Cheap","Small","Light","Short",                   "choiceA"},
//         new String[]{"Correct spelling?",                      "Recieve","Beleive","Receive","Achive",            "choiceC"},
//         new String[]{"Noun of educate?",                       "Educating","Educated","Education","Educator",     "choiceC"},
//         new String[]{"She has ___ her homework.",              "do","did","does","done",                           "choiceD"},
//         new String[]{"Quick is an?",                           "Noun","Verb","Adjective","Adverb",                "choiceC"},
//         new String[]{"I ___ to the market yesterday.",         "go","goes","went","gone",                         "choiceC"},
//         new String[]{"Plural of child?",                       "childs","childes","children","childrens",         "choiceC"},
//         new String[]{"Antonym of ancient?",                    "Old","Historic","Modern","Antique",               "choiceC"},
//         new String[]{"Which is a conjunction?",                "Quickly","Beautiful","And","Run",                 "choiceC"},
//         new String[]{"He runs fast — fast is an?",             "Noun","Adjective","Adverb","Preposition",         "choiceC"},
//         new String[]{"Passive of: The cat caught the mouse?",  "Mouse was caught by cat","Cat was caught","Mouse caught cat","Cat caught mouse","choiceA"},
//         new String[]{"Correct sentence?",                      "She don't like it","She doesn't likes it","She doesn't like it","She not like it","choiceC"},
//         new String[]{"Comparative of good?",                   "Gooder","More good","Better","Goodest",           "choiceC"},
//         new String[]{"Which word is a preposition?",           "Run","On","Happy","Quickly",                      "choiceB"},
//         new String[]{"Photosynthesis has how many syllables?", "4","5","6","7",                                   "choiceB"},
//         new String[]{"I wish I ___ fly.",                      "can","could","will","would",                      "choiceB"},
//         new String[]{"Synonym of enormous?",                   "Tiny","Huge","Average","Narrow",                  "choiceB"},
//         new String[]{"Isn't is a contraction of?",             "is not","it is not","was not","in not",           "choiceA"},
//         new String[]{"Which is a proper noun?",                "city","river","Cairo","country",                  "choiceC"},
//         new String[]{"Future simple: I ___ travel tomorrow.",  "travel","travelled","am travel","will travel",    "choiceD"},
//         new String[]{"Adverb form of loud?",                   "Louding","Louder","Loudly","Louds",               "choiceC"},
//         new String[]{"Which sentence is interrogative?",       "Go home.","What time is it?","Stay here!","It is late.","choiceB"},
//         new String[]{"Between is used for ___ items.",         "one","two","three","many",                        "choiceB"},
//         new String[]{"He is taller ___ his brother.",          "then","than","as","like",                         "choiceB"},
//         new String[]{"Plural of analysis?",                    "analysises","analysies","analyses","analysiss",   "choiceC"}
//     );

//     private static final List<String[]> ARABIC_BANK = List.of(
//         new String[]{"مضاد كلمة (سعيد)؟",                   "حزين","فرح","جميل","قوي",              "choiceA"},
//         new String[]{"جمع كلمة (قلم)؟",                     "أقلام","قلمان","قلمون","قلمات",         "choiceA"},
//         new String[]{"مفرد كلمة (كتب)؟",                    "كتبة","كتاب","كتابة","كتبات",           "choiceB"},
//         new String[]{"مرادف كلمة (جميل)؟",                  "قبيح","حسن","كبير","صغير",              "choiceB"},
//         new String[]{"الفعل الماضي من (يكتب)؟",             "اكتب","كتب","يكتبون","كاتب",            "choiceB"},
//         new String[]{"جمع (مدرسة)؟",                        "مدارس","مدرستان","مدارسة","مدرسون",     "choiceA"},
//         new String[]{"مضاد (ضعيف)؟",                        "صغير","قوي","ثقيل","بعيد",              "choiceB"},
//         new String[]{"الفعل المضارع من (كتب)؟",             "يكتب","كتب","اكتب","كاتب",             "choiceA"},
//         new String[]{"المؤنث من (معلم)؟",                   "معلمة","معلمات","معلمون","معلمين",      "choiceA"},
//         new String[]{"مرادف (كبير)؟",                       "صغير","ضخم","خفيف","قصير",              "choiceB"},
//         new String[]{"جمع (باب)؟",                          "أبواب","بابان","أبوابة","بوابات",        "choiceA"},
//         new String[]{"مفرد (أولاد)؟",                       "ولدان","وليد","ولد","ولدة",              "choiceC"},
//         new String[]{"مضاد (قريب)؟",                        "أمام","وسط","بعيد","جانب",              "choiceC"},
//         new String[]{"الاسم من (يتعلم)؟",                   "تعلم","تعليم","معلم","متعلم",           "choiceB"},
//         new String[]{"جمع (مدينة)؟",                        "مدن","مدينتان","مدينات","مدنيون",       "choiceA"},
//         new String[]{"الفعل الأمر من (يجلس)؟",             "يجلسون","جلس","اجلس","جلسة",            "choiceC"},
//         new String[]{"مرادف (حزين)؟",                       "فرحان","سعيد","كئيب","مبسوط",          "choiceC"},
//         new String[]{"كلمة (الشمس) نوعها؟",                "فعل","اسم","حرف","صفة",                 "choiceB"},
//         new String[]{"جمع (كتاب)؟",                         "كتابان","كتابة","كتب","كتابون",          "choiceC"},
//         new String[]{"مضاد (داخل)؟",                        "أمام","خارج","يمين","فوق",              "choiceB"},
//         new String[]{"مفرد (أسماء)؟",                       "اسمان","سماء","اسم","مسمى",             "choiceC"},
//         new String[]{"حرف الجر في: ذهبت ___ المدرسة؟",    "في","من","إلى","على",                   "choiceC"},
//         new String[]{"مضاد (بطيء)؟",                        "ثقيل","كبير","سريع","صغير",             "choiceC"},
//         new String[]{"(هم) ضمير؟",                          "مفرد مذكر","مفرد مؤنث","جمع مذكر","جمع مؤنث","choiceC"},
//         new String[]{"جمع (شجرة)؟",                         "شجرتان","أشجار","شجرات","شجارة",        "choiceB"},
//         new String[]{"كلمة (يركض) هي؟",                    "اسم","فعل","حرف","ضمير",               "choiceB"},
//         new String[]{"مرادف (صحيح)؟",                       "خطأ","غلط","سليم","بعيد",              "choiceC"},
//         new String[]{"مؤنث (أسد)؟",                         "أسدة","أسادة","لبؤة","سودة",            "choiceC"},
//         new String[]{"أسلوب التعجب في (ما أجمل البحر!)؟", "نفي","تعجب","استفهام","أمر",            "choiceB"},
//         new String[]{"جمع (ولد)؟",                          "ولدان","أولاد","ولدة","ولادة",           "choiceB"},
//         new String[]{"مضاد (صغير)؟",                        "طويل","قصير","كبير","ثقيل",             "choiceC"},
//         new String[]{"الفعل المبني للمجهول من (كتب الطالب)؟","كتب","كتب بالمجهول","يكتب","كاتب",   "choiceB"}
//     );

//     private static final List<String[]> CS_BANK = List.of(
//         new String[]{"CPU stands for?",                         "Central Process Unit","Central Processing Unit","Control Power Unit","Computer Personal Unit","choiceB"},
//         new String[]{"Binary uses digits?",                     "0 and 1","1 and 2","A and B","5 and 10",            "choiceA"},
//         new String[]{"RAM stands for?",                         "Read Access Memory","Random Access Memory","Read And Modify","Rapid Access Module","choiceB"},
//         new String[]{"Which is an input device?",               "Monitor","Printer","Keyboard","Speaker",             "choiceC"},
//         new String[]{"Which is an output device?",              "Mouse","Scanner","Keyboard","Printer",              "choiceD"},
//         new String[]{"1 byte = ? bits",                         "4","8","16","32",                                   "choiceB"},
//         new String[]{"HTML stands for?",                        "Hyper Text Markup Language","High Text Main Language","Hyper Transfer Markup Layer","Home Text Markup Link","choiceA"},
//         new String[]{"What does Save As do?",                   "Closes file","Saves with new name","Prints file","Opens file","choiceB"},
//         new String[]{"Which is NOT a programming language?",    "Java","Python","HTML","C++",                        "choiceC"},
//         new String[]{"Operating system example?",               "Google Chrome","Microsoft Word","Windows 11","Python","choiceC"},
//         new String[]{"Internet uses which protocol?",           "USB","HTTP","HDMI","Bluetooth",                     "choiceB"},
//         new String[]{"Which stores data permanently?",          "RAM","Cache","ROM","Register",                      "choiceC"},
//         new String[]{"Mouse is a ___ device.",                  "Output","Processing","Input","Storage",             "choiceC"},
//         new String[]{"Which extension is an image?",            ".mp3",".docx",".png",".exe",                        "choiceC"},
//         new String[]{"A database stores data in?",              "Images","Tables","Slides","Videos",                 "choiceB"},
//         new String[]{"10 decimal = ? binary",                   "1000","1001","1010","1011",                         "choiceC"},
//         new String[]{"Which is a web browser?",                 "Ubuntu","Firefox","Photoshop","Excel",              "choiceB"},
//         new String[]{"Encryption is used for?",                 "Speed","Printing","Security","Compression",         "choiceC"},
//         new String[]{"Resolution is measured in?",              "Hz","GB","Pixels","Watts",                          "choiceC"},
//         new String[]{"CSS is used for?",                        "Logic","Styling","Database","Networking",           "choiceB"},
//         new String[]{"Algorithm is?",                           "A virus","Step-by-step instructions","A hardware part","An OS","choiceB"},
//         new String[]{"Ctrl+Z is used for?",                     "Save","Cut","Undo","Copy",                          "choiceC"},
//         new String[]{"Which memory is faster?",                 "HDD","SSD","RAM","DVD",                             "choiceC"},
//         new String[]{"IP address identifies?",                  "Software","Device on network","File type","Processor speed","choiceB"},
//         new String[]{"Phishing is a type of?",                  "Programming","Cyber attack","Hardware failure","Algorithm","choiceB"},
//         new String[]{"What does a compiler do?",                "Runs hardware","Converts code to machine code","Displays output","Stores files","choiceB"},
//         new String[]{"Which is a spreadsheet app?",             "Word","Notepad","Excel","PowerPoint",               "choiceC"},
//         new String[]{"Wi-Fi stands for?",                       "Wire Fidelity","Wireless Fidelity","Wide Frequency","Web Interface","choiceB"},
//         new String[]{"USB stands for?",                         "Universal Serial Bus","Unified System Board","Ultra Speed Boot","Universal Storage Bus","choiceA"},
//         new String[]{"Which language is used for web pages?",   "Java","C++","HTML","Assembly",                      "choiceC"},
//         new String[]{"Virus in computing is?",                  "A helpful program","Malicious software","A database","An OS","choiceB"},
//         new String[]{"Which of these is a search engine?",      "Amazon","Netflix","Google","Spotify",               "choiceC"}
//     );

//     // =========================================================================

//     @Override
//     @Transactional
//     public void run(String... args) {

//         if (!seedForce && assessmentRepo.count() > 0) {
//             System.out.println("Seeder already executed. Skipping...");
//             return;
//         }

//         Random rnd = new Random(42);

//         List<Level>   levels   = levelRepo.findAll();
//         List<Subject> subjects = subjectRepo.findAll();
//         List<Teacher> teachers = teacherRepo.findAll();
//         List<Student> students = studentRepo.findAll();

//         if (subjects.isEmpty() || teachers.isEmpty() || students.isEmpty()) {
//             System.out.println("Missing base data (subjects/teachers/students). Seeder stopped.");
//             return;
//         }

//         // ── DEBUG: print teacher→subject mapping so we can verify ──────────
//         System.out.println("[DEBUG] Teacher subject mapping sample:");
//         teachers.stream().limit(10).forEach(t -> {
//             String sInfo = (t.getSubject() == null)
//                 ? "subject=NULL"
//                 : "subjectId=" + t.getSubject().getId() + " name=" + t.getSubject().getName();
//             System.out.println("  teacher.id=" + t.getId() + "  " + sInfo);
//         });
//         // ───────────────────────────────────────────────────────────────────

//         System.out.println("Seeding Assessments + Questions + Scores...");

//         int assessmentCounter = 1;
//         int examsPerLevel       = 2;
//         int assignmentsPerLevel = 3;

//         // Build a lookup: subjectId -> Teacher for fast access
//         Map<Integer, Teacher> teacherBySubjectId = new HashMap<>();
//         for (Teacher t : teachers) {
//             if (t.getSubject() != null) {
//                 teacherBySubjectId.put(t.getSubject().getId(), t);
//             }
//         }

//         // Group subjects by level
//         Map<Integer, List<Subject>> subjectsByLevel = subjects.stream()
//             .filter(s -> s.getLevel() != null)
//             .collect(Collectors.groupingBy(s -> s.getLevel().getId()));

//         for (Map.Entry<Integer, List<Subject>> entry : subjectsByLevel.entrySet()) {
//             int           levelId       = entry.getKey();
//             List<Subject> levelSubjects = entry.getValue();

//             Level level = levels.stream()
//                 .filter(l -> l.getId() == levelId)
//                 .findFirst().orElse(null);

//             String levelName = (level != null) ? level.getName() : "Level " + levelId;

//             List<Student> levelStudents = students.stream()
//                 .filter(st -> st.getLevel() != null && st.getLevel().getId() == levelId)
//                 .collect(Collectors.toList());

//             System.out.printf("  %s : %d subjects, %d students%n",
//                 levelName, levelSubjects.size(), levelStudents.size());

//             for (Subject subject : levelSubjects) {

//                 // Lookup teacher by this exact subject id
//                 Teacher teacher = teacherBySubjectId.get(subject.getId());

//                 if (teacher == null) {
//                     // Fallback: find any teacher whose subject name matches (any level)
//                     teacher = teachers.stream()
//                         .filter(t -> t.getSubject() != null
//                             && subject.getName() != null
//                             && subject.getName().equalsIgnoreCase(t.getSubject().getName()))
//                         .findFirst().orElse(null);
//                 }

//                 if (teacher == null) {
//                     System.out.println("    [WARN] No teacher found for: "
//                         + subject.getName() + " (id=" + subject.getId() + ") — skipping.");
//                     continue;
//                 }

//                 List<String[]> bank = bankFor(subject.getName());

//                 // EXAMs
//                 for (int i = 0; i < examsPerLevel; i++) {
//                     Assessment exam = buildAssessment(
//                         subject.getName() + " Exam " + assessmentCounter++,
//                         AssessmentType.EXAM, subject, teacher);
//                     exam = assessmentRepo.save(exam);
//                     attachQuestions(exam, bank, rnd);
//                     createScores(exam, levelStudents, teacher, subject, rnd);
//                 }

//                 // ASSIGNMENTs
//                 for (int i = 0; i < assignmentsPerLevel; i++) {
//                     Assessment assignment = buildAssessment(
//                         subject.getName() + " Assignment " + assessmentCounter++,
//                         AssessmentType.ASSIGNMENT, subject, teacher);
//                     assignment = assessmentRepo.save(assignment);
//                     attachQuestions(assignment, bank, rnd);
//                     createScores(assignment, levelStudents, teacher, subject, rnd);
//                 }
//             }
//         }

//         System.out.println("Seeder completed successfully.");
//     }

//     // =========================================================================
//     // HELPERS
//     // =========================================================================

//     private Assessment buildAssessment(String title, AssessmentType type,
//                                         Subject subject, Teacher teacher) {
//         Assessment a = new Assessment();
//         a.setTitle(title);
//         a.setAssessmentType(type);
//         a.setSubject(subject);
//         a.setTeacher(teacher);
//         return a;
//     }

//     private void attachQuestions(Assessment assessment, List<String[]> bank, Random rnd) {
//         int needed = assessment.getNumOfQues();

//         List<String[]> shuffled = new ArrayList<>(bank);
//         Collections.shuffle(shuffled, rnd);

//         List<Question> questions = new ArrayList<>();
//         for (int q = 0; q < needed; q++) {
//             String[] tpl = shuffled.get(q % shuffled.size());

//             Question question = new Question();
//             question.setHeadline(tpl[0]);
//             question.setChoices(List.of(tpl[1], tpl[2], tpl[3], tpl[4]));
//             question.setCorrect(CorrectChoice.valueOf(tpl[5]));
//             question.setAssessment(assessment);
//             questions.add(questionRepo.save(question));
//         }

//         assessment.setQuestions(questions);
//         assessmentRepo.save(assessment);
//     }

//     private void createScores(Assessment assessment, List<Student> students,
//                                Teacher teacher, Subject subject, Random rnd) {
//         for (Student student : students) {
//             Score score = new Score(student, assessment, rnd.nextInt(101), true);
//             score.setTeacher(teacher);
//             score.setSubject(subject);
//             score.setAssessment(assessment);
//             scoreRepo.save(score);
//         }
//     }

//     private List<String[]> bankFor(String subjectName) {
//         if (subjectName == null) return MATH_BANK;
//         return switch (subjectName) {
//             case "Math"     -> MATH_BANK;
//             case "Science"  -> SCIENCE_BANK;
//             case "English"  -> ENGLISH_BANK;
//             case "Arabic"   -> ARABIC_BANK;
//             case "Computer" -> CS_BANK;
//             default         -> MATH_BANK;
//         };
//     }
// }