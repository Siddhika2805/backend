import sys
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN
from pptx.enum.shapes import MSO_SHAPE

def create_presentation():
    prs = Presentation()
    # 16:9 widescreen dimensions
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6]

    # Theme colors
    COLOR_BG = RGBColor(248, 250, 252)       # Slate 50
    COLOR_NAVY = RGBColor(15, 23, 42)        # Slate 900
    COLOR_BLUE = RGBColor(37, 99, 235)       # Blue 600
    COLOR_LIGHT_BLUE = RGBColor(239, 246, 255) # Blue 50
    COLOR_TEXT_DARK = RGBColor(30, 41, 59)   # Slate 800
    COLOR_TEXT_MUTED = RGBColor(100, 116, 139) # Slate 500
    COLOR_WHITE = RGBColor(255, 255, 255)
    COLOR_CARD_BORDER = RGBColor(226, 232, 240)
    COLOR_ACCENT = RGBColor(16, 185, 129)    # Emerald 500

    def add_bg(slide):
        bg = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, prs.slide_width, prs.slide_height)
        bg.fill.solid()
        bg.fill.fore_color.rgb = COLOR_BG
        bg.line.fill.background() # No border
        return bg

    def add_header(slide, title, category="InterviewHub"):
        cat_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.4), Inches(11), Inches(0.4))
        tf_cat = cat_box.text_frame
        tf_cat.word_wrap = True
        p_cat = tf_cat.paragraphs[0]
        p_cat.text = category.upper()
        p_cat.font.size = Pt(11)
        p_cat.font.bold = True
        p_cat.font.color.rgb = COLOR_BLUE

        title_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.7), Inches(11.5), Inches(0.7))
        tf_title = title_box.text_frame
        tf_title.word_wrap = True
        p_title = tf_title.paragraphs[0]
        p_title.text = title
        p_title.font.size = Pt(24)
        p_title.font.bold = True
        p_title.font.color.rgb = COLOR_NAVY

    # =========================================================================
    # SLIDE 1: Title Slide
    # =========================================================================
    s1 = prs.slides.add_slide(blank_layout)
    bg1 = s1.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, prs.slide_width, prs.slide_height)
    bg1.fill.solid()
    bg1.fill.fore_color.rgb = COLOR_NAVY
    bg1.line.fill.background()

    # Title container
    tb1 = s1.shapes.add_textbox(Inches(1.0), Inches(1.2), Inches(11.3), Inches(4.5))
    tf1 = tb1.text_frame
    tf1.word_wrap = True

    p_intro = tf1.paragraphs[0]
    p_intro.text = "PROJECT PRESENTATION ON"
    p_intro.font.size = Pt(14)
    p_intro.font.bold = True
    p_intro.font.color.rgb = RGBColor(147, 197, 253) # Light Blue 300

    p_main = tf1.add_paragraph()
    p_main.text = "InterviewHub"
    p_main.font.size = Pt(44)
    p_main.font.bold = True
    p_main.font.color.rgb = COLOR_WHITE
    p_main.space_before = Pt(8)

    p_sub = tf1.add_paragraph()
    p_sub.text = "Multi-Interviewer Interview Management & Performance Tracking System"
    p_sub.font.size = Pt(20)
    p_sub.font.color.rgb = RGBColor(203, 213, 225) # Slate 300
    p_sub.space_before = Pt(8)

    p_dept = tf1.add_paragraph()
    p_dept.text = "Department of Computer Science and Engineering, Chitkara University, Punjab"
    p_dept.font.size = Pt(14)
    p_dept.font.color.rgb = RGBColor(148, 163, 184)
    p_dept.space_before = Pt(20)

    # Submitted by & Submitted to container (2 columns card)
    box_meta = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(1.0), Inches(4.8), Inches(11.33), Inches(1.8))
    box_meta.fill.solid()
    box_meta.fill.fore_color.rgb = RGBColor(30, 41, 59)
    box_meta.line.color.rgb = RGBColor(51, 65, 85)

    tb_subby = s1.shapes.add_textbox(Inches(1.3), Inches(4.95), Inches(5.5), Inches(1.5))
    tf_subby = tb_subby.text_frame
    p_sbt = tf_subby.paragraphs[0]
    p_sbt.text = "SUBMITTED BY:"
    p_sbt.font.size = Pt(11)
    p_sbt.font.bold = True
    p_sbt.font.color.rgb = RGBColor(147, 197, 253)

    p_names = tf_subby.add_paragraph()
    p_names.text = "• Siddhika      • Vrinda      • Gunjot      • Parth"
    p_names.font.size = Pt(15)
    p_names.font.bold = True
    p_names.font.color.rgb = COLOR_WHITE
    p_names.space_before = Pt(6)

    p_desc = tf_subby.add_paragraph()
    p_desc.text = "B.E. Computer Science & Engineering (2nd Year)"
    p_desc.font.size = Pt(12)
    p_desc.font.color.rgb = RGBColor(203, 213, 225)
    p_desc.space_before = Pt(4)

    tb_subto = s1.shapes.add_textbox(Inches(7.2), Inches(4.95), Inches(4.8), Inches(1.5))
    tf_subto = tb_subto.text_frame
    p_stt = tf_subto.paragraphs[0]
    p_stt.text = "SUBMITTED TO:"
    p_stt.font.size = Pt(11)
    p_stt.font.bold = True
    p_stt.font.color.rgb = RGBColor(147, 197, 253)

    p_fac = tf_subto.add_paragraph()
    p_fac.text = "Dr. Gagan Singla"
    p_fac.font.size = Pt(16)
    p_fac.font.bold = True
    p_fac.font.color.rgb = COLOR_WHITE
    p_fac.space_before = Pt(6)

    p_facsub = tf_subto.add_paragraph()
    p_facsub.text = "Department of Computer Science & Engineering"
    p_facsub.font.size = Pt(12)
    p_facsub.font.color.rgb = RGBColor(203, 213, 225)
    p_facsub.space_before = Pt(4)

    # =========================================================================
    # SLIDE 2: Problem Statement
    # =========================================================================
    s2 = prs.slides.add_slide(blank_layout)
    add_bg(s2)
    add_header(s2, "Problem Statement & Proposed Solution", "Context & Motivation")

    # 3 problem boxes
    probs = [
        ("1", "Scattered & Disconnected Mock Interviews", 
         "Candidates attend isolated mock rounds with different interviewers without a unified platform to track request approvals, schedule dates, or maintain multi-round histories."),
        ("2", "No Standardized Competency Evaluation", 
         "Feedback is usually verbal or unstructured notes. Without multi-skill rubric scoring (0–100), candidates cannot diagnose specific weaknesses like Problem Solving or System Design."),
        ("3", "No Longitudinal Performance Tracking", 
         "Candidates cannot quantify growth over time. Furthermore, mock practice is disconnected from academic placement drives and eligibility criteria (CGPA/department rules).")
    ]

    card_w = Inches(3.64)
    card_h = Inches(3.6)
    card_y = Inches(1.6)

    for idx, (num, p_title, p_body) in enumerate(probs):
        x = Inches(0.8 + idx * 4.0)
        card = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, x, card_y, card_w, card_h)
        card.fill.solid()
        card.fill.fore_color.rgb = COLOR_WHITE
        card.line.color.rgb = COLOR_CARD_BORDER

        # Number badge
        badge = s2.shapes.add_shape(MSO_SHAPE.OVAL, x + Inches(0.3), card_y + Inches(0.3), Inches(0.6), Inches(0.6))
        badge.fill.solid()
        badge.fill.fore_color.rgb = COLOR_BLUE
        badge.line.fill.background()
        tf_b = badge.text_frame
        p_b = tf_b.paragraphs[0]
        p_b.text = num
        p_b.font.size = Pt(16)
        p_b.font.bold = True
        p_b.font.color.rgb = COLOR_WHITE
        p_b.alignment = PP_ALIGN.CENTER

        # Content
        tb = s2.shapes.add_textbox(x + Inches(0.3), card_y + Inches(1.0), card_w - Inches(0.6), Inches(2.4))
        tf = tb.text_frame
        tf.word_wrap = True

        pt = tf.paragraphs[0]
        pt.text = p_title
        pt.font.size = Pt(15)
        pt.font.bold = True
        pt.font.color.rgb = COLOR_NAVY

        pb = tf.add_paragraph()
        pb.text = p_body
        pb.font.size = Pt(12)
        pb.font.color.rgb = COLOR_TEXT_MUTED
        pb.space_before = Pt(8)

    # Bottom Banner: Our answer
    ans_box = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(5.45), Inches(11.73), Inches(1.4))
    ans_box.fill.solid()
    ans_box.fill.fore_color.rgb = COLOR_LIGHT_BLUE
    ans_box.line.color.rgb = COLOR_BLUE

    tb_ans = s2.shapes.add_textbox(Inches(1.1), Inches(5.55), Inches(11.1), Inches(1.2))
    tf_ans = tb_ans.text_frame
    tf_ans.word_wrap = True
    p_ah = tf_ans.paragraphs[0]
    p_ah.text = "Our Answer: InterviewHub"
    p_ah.font.size = Pt(16)
    p_ah.font.bold = True
    p_ah.font.color.rgb = COLOR_BLUE

    p_at = tf_ans.add_paragraph()
    p_at.text = "A robust two-sided Core Java + MySQL platform connecting candidates and interviewers, enforcing conflict-free slot booking, standardizing 6-skill rubric evaluation, calculating mathematical performance improvement over time, and integrating campus placement drive eligibility."
    p_at.font.size = Pt(12)
    p_at.font.color.rgb = COLOR_TEXT_DARK
    p_at.space_before = Pt(4)

    # =========================================================================
    # SLIDE 3: Project Objectives & Scope
    # =========================================================================
    s3 = prs.slides.add_slide(blank_layout)
    add_bg(s3)
    add_header(s3, "Project Objectives and Scope", "Scope & Boundary Definition")

    # Left Card: Objectives
    obj_card = s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(1.6), Inches(7.3), Inches(5.3))
    obj_card.fill.solid()
    obj_card.fill.fore_color.rgb = COLOR_WHITE
    obj_card.line.color.rgb = COLOR_CARD_BORDER

    tb_obj = s3.shapes.add_textbox(Inches(1.1), Inches(1.8), Inches(6.7), Inches(4.8))
    tf_obj = tb_obj.text_frame
    tf_obj.word_wrap = True

    p_ot = tf_obj.paragraphs[0]
    p_ot.text = "Project Objectives"
    p_ot.font.size = Pt(18)
    p_ot.font.bold = True
    p_ot.font.color.rgb = COLOR_NAVY

    objs = [
        ("1", "Multi-Interviewer Coordination", "Enable candidates to search, filter by specialization, and request Technical or HR interview slots with multiple expert interviewers."),
        ("2", "Standardized Rubric Evaluation", "Allow interviewers to record candidate performance across 6 competency rubrics (0-100) with detailed qualitative remarks."),
        ("3", "Longitudinal Performance Tracking", "Calculate real database-backed growth: Average, Highest, Lowest, First vs Latest score, and Improvement Percentage."),
        ("4", "Thread Safety & Concurrency", "Enforce atomic slot reservation via synchronized SlotManager and asynchronous background task handling via Producer-Consumer queue."),
        ("5", "Campus Placement Integration", "Preserve academic entities (Student, Company, PlacementDrive, Application) and evaluate eligibility via the Eligible interface.")
    ]

    for num, title, desc in objs:
        p = tf_obj.add_paragraph()
        p.text = f"{num}. {title}: {desc}"
        p.font.size = Pt(11.5)
        p.font.color.rgb = COLOR_TEXT_DARK
        p.space_before = Pt(8)

    # Right Card: Scope (In scope / Future work)
    scope_card = s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(8.35), Inches(1.6), Inches(4.18), Inches(5.3))
    scope_card.fill.solid()
    scope_card.fill.fore_color.rgb = COLOR_WHITE
    scope_card.line.color.rgb = COLOR_CARD_BORDER

    tb_sc = s3.shapes.add_textbox(Inches(8.6), Inches(1.8), Inches(3.7), Inches(4.8))
    tf_sc = tb_sc.text_frame
    tf_sc.word_wrap = True

    p_st = tf_sc.paragraphs[0]
    p_st.text = "Project Scope"
    p_st.font.size = Pt(18)
    p_st.font.bold = True
    p_st.font.color.rgb = COLOR_NAVY

    p_in = tf_sc.add_paragraph()
    p_in.text = "✔ In Scope"
    p_in.font.size = Pt(14)
    p_in.font.bold = True
    p_in.font.color.rgb = COLOR_BLUE
    p_in.space_before = Pt(12)

    in_items = [
        "Pure Core Java 17 Console CLI (Scanner)",
        "Layered Architecture (Model-DAO-Service)",
        "MySQL 8.0 Relational Persistence (JDBC)",
        "PBKDF2 Cryptographic Password Hashing",
        "Multithreading & Atomic Check-and-Decrement",
        "Producer-Consumer Task Queue (wait/notify)",
        "13 Automated JUnit 5 Test Suite"
    ]
    for item in in_items:
        p = tf_sc.add_paragraph()
        p.text = f"• {item}"
        p.font.size = Pt(11)
        p.font.color.rgb = COLOR_TEXT_DARK
        p.space_before = Pt(3)

    p_out = tf_sc.add_paragraph()
    p_out.text = "⚡ Future Work"
    p_out.font.size = Pt(14)
    p_out.font.bold = True
    p_out.font.color.rgb = COLOR_TEXT_MUTED
    p_out.space_before = Pt(12)

    out_items = [
        "WebRTC Live Video/Audio Integration",
        "Automated Calendar Sync (Google/Outlook)",
        "Enterprise Web & Mobile Client Frontends"
    ]
    for item in out_items:
        p = tf_sc.add_paragraph()
        p.text = f"• {item}"
        p.font.size = Pt(11)
        p.font.color.rgb = COLOR_TEXT_MUTED
        p.space_before = Pt(3)

    # =========================================================================
    # SLIDE 4: Hardware and Software Interface
    # =========================================================================
    s4 = prs.slides.add_slide(blank_layout)
    add_bg(s4)
    add_header(s4, "Hardware and Software Interface", "System Specifications & Tooling")

    # Hardware Card
    hw_card = s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(1.6), Inches(5.6), Inches(3.6))
    hw_card.fill.solid()
    hw_card.fill.fore_color.rgb = COLOR_WHITE
    hw_card.line.color.rgb = COLOR_CARD_BORDER

    tb_hw = s4.shapes.add_textbox(Inches(1.1), Inches(1.8), Inches(5.0), Inches(3.2))
    tf_hw = tb_hw.text_frame
    tf_hw.word_wrap = True

    p_hwt = tf_hw.paragraphs[0]
    p_hwt.text = "Hardware Interface"
    p_hwt.font.size = Pt(18)
    p_hwt.font.bold = True
    p_hwt.font.color.rgb = COLOR_NAVY

    hw_specs = [
        ("Processor", "Intel Core i3 / AMD Ryzen 3 or higher (Multi-core CPU recommended for multithreaded simulations)"),
        ("Memory (RAM)", "4 GB minimum (8 GB recommended for simultaneous IDE & MySQL server execution)"),
        ("Storage", "Minimum 500 MB free space for JDK 17, Maven local repository (.m2), and MySQL databases"),
        ("Peripherals", "Standard physical keyboard & terminal monitor display for console CLI menu interaction")
    ]
    for lbl, val in hw_specs:
        p = tf_hw.add_paragraph()
        p.text = f"• {lbl}: {val}"
        p.font.size = Pt(11.5)
        p.font.color.rgb = COLOR_TEXT_DARK
        p.space_before = Pt(6)

    # Software Card
    sw_card = s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(6.8), Inches(1.6), Inches(5.73), Inches(3.6))
    sw_card.fill.solid()
    sw_card.fill.fore_color.rgb = COLOR_WHITE
    sw_card.line.color.rgb = COLOR_CARD_BORDER

    tb_sw = s4.shapes.add_textbox(Inches(7.1), Inches(1.8), Inches(5.1), Inches(3.2))
    tf_sw = tb_sw.text_frame
    tf_sw.word_wrap = True

    p_swt = tf_sw.paragraphs[0]
    p_swt.text = "Software Interface"
    p_swt.font.size = Pt(18)
    p_swt.font.bold = True
    p_swt.font.color.rgb = COLOR_NAVY

    sw_specs = [
        ("Language & Platform", "Java 17 (JDK) with pure Object-Oriented Architecture"),
        ("Database Engine", "MySQL 8.0 Server via JDBC (MySQL Connector/J 8.3.0)"),
        ("Interface / CLI", "Interactive menu-driven Console CLI using Scanner over System.in"),
        ("Build & Dependencies", "Apache Maven 3.9+ with portable Windows wrapper (mvnw.cmd)"),
        ("Security / Crypto", "PBKDF2 with HMAC-SHA256 (Java Cryptography Architecture)"),
        ("Automated Testing", "JUnit 5 (JUnit Jupiter 5.10.2) running 13 test suites"),
        ("CI/CD & Version Control", "Git, GitHub & GitHub Actions automated build pipeline")
    ]
    for lbl, val in sw_specs:
        p = tf_sw.add_paragraph()
        p.text = f"• {lbl}: {val}"
        p.font.size = Pt(11)
        p.font.color.rgb = COLOR_TEXT_DARK
        p.space_before = Pt(4)

    # Architecture Connection Banner
    flow_box = s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(5.45), Inches(11.73), Inches(1.4))
    flow_box.fill.solid()
    flow_box.fill.fore_color.rgb = COLOR_LIGHT_BLUE
    flow_box.line.color.rgb = COLOR_BLUE

    tb_flow = s4.shapes.add_textbox(Inches(1.1), Inches(5.55), Inches(11.1), Inches(1.2))
    tf_flow = tb_flow.text_frame
    tf_flow.word_wrap = True

    p_ft = tf_flow.paragraphs[0]
    p_ft.text = "Data Flow & Architecture Pipeline"
    p_ft.font.size = Pt(14)
    p_ft.font.bold = True
    p_ft.font.color.rgb = COLOR_BLUE

    p_diag = tf_flow.add_paragraph()
    p_diag.text = "Console CLI (Scanner)  ⇄  Service Layer (OOP Business Logic)  ⇄  DAO Layer (PreparedStatement)  ⇄  MySQL 8.0 (JDBC Connection)"
    p_diag.font.size = Pt(13)
    p_diag.font.bold = True
    p_diag.font.color.rgb = COLOR_NAVY
    p_diag.space_before = Pt(4)

    p_note = tf_flow.add_paragraph()
    p_note.text = "Transactions (commit/rollback) guarantee consistency; Collections act as in-memory cache; MySQL remains the permanent source of truth."
    p_note.font.size = Pt(11)
    p_note.font.color.rgb = COLOR_TEXT_MUTED
    p_note.space_before = Pt(2)

    # =========================================================================
    # SLIDE 5: System Architecture & Performance Metrics
    # =========================================================================
    s5 = prs.slides.add_slide(blank_layout)
    add_bg(s5)
    add_header(s5, "System Architecture & Performance Metrics", "Three-Tiered Core Java Architecture")

    # 3 Architecture Columns
    col_w = Inches(3.64)
    col_h = Inches(3.6)
    col_y = Inches(1.6)

    tiers = [
        ("Java Console UI", COLOR_BLUE, [
            "Role-based Menus (Candidate, Interviewer, Admin)",
            "Candidate Dashboard: Bookings, History, Drives",
            "Interviewer Portal: Request Review & Feedback",
            "Admin Control Panel: Users, Questions, Drives",
            "Graceful Scanner lifecycle & error traps"
        ]),
        ("Core Logic Layer", COLOR_NAVY, [
            "Encapsulated Domain Models (User, Student, Candidate)",
            "SlotManager with synchronized check-and-decrement",
            "TaskQueue Producer-Consumer (wait/notifyAll)",
            "CollectionManager: O(1) HashMap & Comparator sort",
            "Transaction management in FeedbackDAO"
        ]),
        ("MySQL Relational Storage", COLOR_ACCENT, [
            "users, candidates, interviewers (Profiles)",
            "interview_requests & interviews (Scheduling)",
            "interview_feedback & skill_scores (Rubrics)",
            "companies, placement_drives, applications",
            "questions (Technical & HR Bank)"
        ])
    ]

    for idx, (title, color, items) in enumerate(tiers):
        x = Inches(0.8 + idx * 4.0)
        card = s5.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, x, col_y, col_w, col_h)
        card.fill.solid()
        card.fill.fore_color.rgb = COLOR_WHITE
        card.line.color.rgb = COLOR_CARD_BORDER

        # Header tag
        tag = s5.shapes.add_shape(MSO_SHAPE.RECTANGLE, x, col_y, col_w, Inches(0.5))
        tag.fill.solid()
        tag.fill.fore_color.rgb = color
        tag.line.fill.background()
        tf_tag = tag.text_frame
        p_tag = tf_tag.paragraphs[0]
        p_tag.text = title
        p_tag.font.size = Pt(13)
        p_tag.font.bold = True
        p_tag.font.color.rgb = COLOR_WHITE
        p_tag.alignment = PP_ALIGN.CENTER

        tb = s5.shapes.add_textbox(x + Inches(0.2), col_y + Inches(0.6), col_w - Inches(0.4), col_h - Inches(0.7))
        tf = tb.text_frame
        tf.word_wrap = True
        for i_idx, item in enumerate(items):
            p = tf.paragraphs[0] if i_idx == 0 else tf.add_paragraph()
            p.text = f"• {item}"
            p.font.size = Pt(11)
            p.font.color.rgb = COLOR_TEXT_DARK
            p.space_before = Pt(4)

    # Bottom Metric Card
    metric_card = s5.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(5.45), Inches(11.73), Inches(1.4))
    metric_card.fill.solid()
    metric_card.fill.fore_color.rgb = COLOR_WHITE
    metric_card.line.color.rgb = COLOR_BLUE

    tb_met = s5.shapes.add_textbox(Inches(1.1), Inches(5.55), Inches(11.1), Inches(1.2))
    tf_met = tb_met.text_frame
    tf_met.word_wrap = True

    p_mh = tf_met.paragraphs[0]
    p_mh.text = "Mathematical Performance Improvement Tracking Formula & 6 Competency Rubrics"
    p_mh.font.size = Pt(13)
    p_mh.font.bold = True
    p_mh.font.color.rgb = COLOR_BLUE

    p_fml = tf_met.add_paragraph()
    p_fml.text = "Improvement Percentage = ((Latest Score - First Score) / First Score) * 100   [Handles division-by-zero safely]"
    p_fml.font.size = Pt(12)
    p_fml.font.bold = True
    p_fml.font.color.rgb = COLOR_NAVY
    p_fml.space_before = Pt(3)

    p_rub = tf_met.add_paragraph()
    p_rub.text = "Evaluated Rubrics (0–100): 1. Technical Knowledge | 2. Problem Solving | 3. Communication | 4. Confidence | 5. Logical Thinking | 6. Subject Knowledge"
    p_rub.font.size = Pt(11)
    p_rub.font.color.rgb = COLOR_TEXT_MUTED
    p_rub.space_before = Pt(3)

    # =========================================================================
    # SLIDE 6: Team and Module Allocation
    # =========================================================================
    s6 = prs.slides.add_slide(blank_layout)
    add_bg(s6)
    add_header(s6, "Team and Module Allocation", "Work Breakdown Structure")

    members = [
        ("MEMBER 1", "Siddhika", "Core OOP Architecture & Admin Governance", [
            "Project architecture & base User model hierarchy",
            "Admin model, system control panel & governance",
            "PBKDF2 HMAC-SHA256 password hashing & salt",
            "User activation / deactivation & interviewer onboarding",
            "Question bank management & global platform stats",
            "10 Functional Modules"
        ]),
        ("MEMBER 2", "Vrinda", "Candidate Portal & Placement Eligibility", [
            "Candidate registration, authentication & profile updates",
            "Interviewer discovery & specialization search filters",
            "Interview request workflow (Technical & HR rounds)",
            "Integration of academic Student, Company, Drive entities",
            "Eligible interface implementation for drive validation",
            "9 Functional Modules"
        ]),
        ("MEMBER 3", "Gunjot", "Interviewer Portal & Rubric Evaluation", [
            "Interviewer login, expertise & profile management",
            "Incoming request review pipeline (Accept / Reject)",
            "Interview question bank selection & round execution",
            "Standardized rubric evaluation (6 skill dimensions)",
            "FeedbackDAO atomic transaction commit & rollback",
            "9 Functional Modules"
        ]),
        ("MEMBER 4", "Parth", "Performance Analytics, Concurrency & Collections", [
            "Longitudinal growth calculation (First/Latest/Avg/% Growth)",
            "CollectionManager: ArrayList, HashMap O(1), HashSet",
            "Comparator sorting (CGPA, Name, Date) & Top 5 ranking",
            "SlotManager synchronized critical section (2 slots/5 threads)",
            "TaskQueue Producer-Consumer & ExecutorService pool",
            "10 Functional Modules"
        ])
    ]

    m_card_w = Inches(2.78)
    m_card_h = Inches(5.3)
    m_card_y = Inches(1.6)

    for idx, (m_tag, name, role, duties) in enumerate(members):
        x = Inches(0.8 + idx * 3.0)
        card = s6.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, x, m_card_y, m_card_w, m_card_h)
        card.fill.solid()
        card.fill.fore_color.rgb = COLOR_WHITE
        card.line.color.rgb = COLOR_CARD_BORDER

        # Header tag
        tag = s6.shapes.add_shape(MSO_SHAPE.RECTANGLE, x, m_card_y, m_card_w, Inches(0.75))
        tag.fill.solid()
        tag.fill.fore_color.rgb = COLOR_NAVY if idx % 2 == 0 else COLOR_BLUE
        tag.line.fill.background()
        tf_tag = tag.text_frame
        p_t1 = tf_tag.paragraphs[0]
        p_t1.text = m_tag
        p_t1.font.size = Pt(10)
        p_t1.font.bold = True
        p_t1.font.color.rgb = RGBColor(147, 197, 253)
        p_t1.alignment = PP_ALIGN.CENTER

        p_t2 = tf_tag.add_paragraph()
        p_t2.text = name
        p_t2.font.size = Pt(14)
        p_t2.font.bold = True
        p_t2.font.color.rgb = COLOR_WHITE
        p_t2.alignment = PP_ALIGN.CENTER

        tb = s6.shapes.add_textbox(x + Inches(0.15), m_card_y + Inches(0.85), m_card_w - Inches(0.3), m_card_h - Inches(0.95))
        tf = tb.text_frame
        tf.word_wrap = True

        p_r = tf.paragraphs[0]
        p_r.text = role
        p_r.font.size = Pt(11)
        p_r.font.bold = True
        p_r.font.color.rgb = COLOR_BLUE
        p_r.space_before = Pt(2)

        for d in duties[:-1]:
            p = tf.add_paragraph()
            p.text = f"• {d}"
            p.font.size = Pt(9.5)
            p.font.color.rgb = COLOR_TEXT_DARK
            p.space_before = Pt(3)

        # Highlight count
        p_cnt = tf.add_paragraph()
        p_cnt.text = f"✔ {duties[-1]}"
        p_cnt.font.size = Pt(10.5)
        p_cnt.font.bold = True
        p_cnt.font.color.rgb = COLOR_ACCENT
        p_cnt.space_before = Pt(6)

    # =========================================================================
    # SLIDE 7: Thank You Slide
    # =========================================================================
    s7 = prs.slides.add_slide(blank_layout)
    bg7 = s7.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, prs.slide_width, prs.slide_height)
    bg7.fill.solid()
    bg7.fill.fore_color.rgb = COLOR_NAVY
    bg7.line.fill.background()

    tb7 = s7.shapes.add_textbox(Inches(1.5), Inches(1.8), Inches(10.33), Inches(4.0))
    tf7 = tb7.text_frame
    tf7.word_wrap = True

    p7_1 = tf7.paragraphs[0]
    p7_1.text = "Thank You!"
    p7_1.font.size = Pt(48)
    p7_1.font.bold = True
    p7_1.font.color.rgb = COLOR_WHITE
    p7_1.alignment = PP_ALIGN.CENTER

    p7_2 = tf7.add_paragraph()
    p7_2.text = "InterviewHub  |  Questions & Discussion"
    p7_2.font.size = Pt(22)
    p7_2.font.bold = True
    p7_2.font.color.rgb = RGBColor(147, 197, 253)
    p7_2.alignment = PP_ALIGN.CENTER
    p7_2.space_before = Pt(12)

    p7_3 = tf7.add_paragraph()
    p7_3.text = "Multi-Interviewer Interview Management & Performance Tracking System\nDepartment of Computer Science and Engineering, Chitkara University, Punjab"
    p7_3.font.size = Pt(14)
    p7_3.font.color.rgb = RGBColor(203, 213, 225)
    p7_3.alignment = PP_ALIGN.CENTER
    p7_3.space_before = Pt(16)

    p7_team = tf7.add_paragraph()
    p7_team.text = "Presented by: Siddhika  •  Vrinda  •  Gunjot  •  Parth"
    p7_team.font.size = Pt(16)
    p7_team.font.bold = True
    p7_team.font.color.rgb = COLOR_WHITE
    p7_team.alignment = PP_ALIGN.CENTER
    p7_team.space_before = Pt(24)

    # Save presentation
    output_path = "InterviewHub_Presentation.pptx"
    prs.save(output_path)
    print(f"Presentation successfully created at {output_path}")

if __name__ == "__main__":
    create_presentation()
