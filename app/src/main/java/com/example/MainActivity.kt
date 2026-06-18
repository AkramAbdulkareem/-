package com.example

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.util.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBG),
          contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
          CalculatorDashboardScreen(
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

// Data Classes & Sealed Classes
enum class CalculatorTab(val title: String, val icon: ImageVector) {
  SCHOOL_EXEMPT("الإعفاء المدرسي", Icons.Default.School),
  SMART_CALC("الحاسبة الذكية", Icons.Default.Calculate),
  CONVERTER("تحويل الوحدات", Icons.Default.SwapHoriz),
  CURRENCY("تحويل العملات", Icons.Default.AttachMoney),
  BMI("مؤشر الجسم BMI", Icons.Default.FitnessCenter),
  AGE("حساب العمر", Icons.Default.Cake),
  DISCOUNT("حساب الخصوم", Icons.Default.Percent),
  LOAN("القروض والتمويل", Icons.Default.AccountBalance)
}

@Composable
fun CalculatorDashboardScreen(modifier: Modifier = Modifier) {
  var currentTab by remember { mutableStateOf(CalculatorTab.SCHOOL_EXEMPT) }
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBG)
  ) {
    // 1. Premium Header with Golden Akram Al-Jabouri Brand Signature
    DesignerSignatureHeader()

    // 2. Horizontal Scrollable Tabs Selection
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      CalculatorTab.values().forEach { tab ->
        val isSelected = currentTab == tab
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
              if (isSelected) {
                Brush.horizontalGradient(listOf(GoldAccent, GoldDim))
              } else {
                Brush.horizontalGradient(listOf(CardSlate, CardSlate))
              }
            )
            .border(
              width = 1.dp,
              color = if (isSelected) GoldAccent else Color.White.copy(alpha = 0.05f),
              shape = RoundedCornerShape(16.dp)
            )
            .clickable { currentTab = tab }
            .padding(horizontal = 16.dp, vertical = 12.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = tab.icon,
              contentDescription = tab.title,
              tint = if (isSelected) ObsidianBG else GoldAccent,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = tab.title,
              color = if (isSelected) ObsidianBG else WhiteIce,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.SansSerif
            )
          }
        }
      }
    }

    // Divider
    HorizontalDivider(
      color = Color.White.copy(alpha = 0.05f),
      thickness = 1.dp,
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    // 3. Dynamic Screen Switcher
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
    ) {
      AnimatedContent(
        targetState = currentTab,
        transitionSpec = {
          fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
        },
        label = "DashboardTabContent"
      ) { targetTab ->
        when (targetTab) {
          CalculatorTab.SCHOOL_EXEMPT -> SchoolExemptionView()
          CalculatorTab.SMART_CALC -> SmartCalculatorView()
          CalculatorTab.CONVERTER -> UnitConverterView()
          CalculatorTab.CURRENCY -> CurrencyConverterView()
          CalculatorTab.BMI -> BmiCalculatorView()
          CalculatorTab.AGE -> AgeCalculatorView()
          CalculatorTab.DISCOUNT -> DiscountCalculatorView()
          CalculatorTab.LOAN -> LoanCalculatorView()
        }
      }
    }

    // 4. Elegant Persistent Copyright Footer
    CopyrightFooter()
  }
}

@Composable
fun DesignerSignatureHeader() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 14.dp)
      .clip(RoundedCornerShape(20.dp))
      .background(
        Brush.verticalGradient(
          listOf(CardSlate.copy(alpha = 0.9f), CardSlate.copy(alpha = 0.6f))
        )
      )
      .border(
        width = 1.dp,
        brush = Brush.horizontalGradient(
          listOf(GoldAccent.copy(alpha = 0.4f), Color.Transparent, GoldAccent.copy(alpha = 0.4f))
        ),
        shape = RoundedCornerShape(20.dp)
      )
      .padding(16.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "الآلة الحاسبة الاحترافية الشاملة",
        color = GoldAccent,
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Brush,
          contentDescription = "Designer Logo",
          tint = GoldAccent.copy(alpha = 0.8f),
          modifier = Modifier.size(14.dp)
        )
        Text(
          text = "أكرم عبدالكريم الجبوري",
          color = WhiteIce.copy(alpha = 0.9f),
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          letterSpacing = 0.5.sp
        )
      }
    }
  }
}

@Composable
fun CopyrightFooter() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(CardSlate.copy(alpha = 0.4f))
      .padding(vertical = 10.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "أكرم عبدالكريم الجبوري",
      color = GrayCool,
      fontSize = 11.sp,
      fontWeight = FontWeight.Normal,
      textAlign = TextAlign.Center
    )
    Text(
      text = "Akram Abdul Kareem Al-Jabouri",
      color = GoldDim.copy(alpha = 0.7f),
      fontSize = 10.sp,
      fontWeight = FontWeight.Light,
      textAlign = TextAlign.Center,
      letterSpacing = 0.8.sp
    )
  }
}

// -------------------------------------------------------------
// MODULE 1: SMART CALCULATOR VIEW
// -------------------------------------------------------------
@Composable
fun SmartCalculatorView() {
  val haptic = LocalHapticFeedback.current
  var expression by remember { mutableStateOf("") }
  var resultText by remember { mutableStateOf("0") }
  var previousHistory by remember { mutableStateOf(listOf<String>()) }
  var isScientific by remember { mutableStateOf(false) }
  var showHistorySheet by remember { mutableStateOf(false) }

  // Perform automatic execution for dynamic previews
  LaunchedEffect(expression) {
    if (expression.isEmpty()) {
      resultText = "0"
    } else {
      try {
        val calculated = evaluateExpression(expression)
        val formattedResult = formatDouble(calculated)
        resultText = formattedResult
      } catch (e: Exception) {
        // Clear preview indicator if math syntax is incomplete
      }
    }
  }

  fun appendToExpr(char: String) {
    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    expression += char
  }

  fun onCalculate() {
    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    if (expression.isEmpty()) return
    try {
      val finalVal = evaluateExpression(expression)
      val formattedResult = formatDouble(finalVal)
      val newHistoryRecord = "$expression = $formattedResult"
      previousHistory = (listOf(newHistoryRecord) + previousHistory).take(15)
      expression = formattedResult
      resultText = formattedResult
    } catch (e: Exception) {
      resultText = "خطأ في الصيغة"
    }
  }

  fun backspace() {
    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    if (expression.isNotEmpty()) {
      expression = expression.dropLast(1)
    }
  }

  fun clearAll() {
    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    expression = ""
    resultText = "0"
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
  ) {
    // A. Display Area
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .clip(RoundedCornerShape(24.dp))
        .background(CardSlate)
        .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(24.dp))
        .padding(18.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
      ) {
        // Top Toolbar: Toggle Scientific & View History
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = {
              haptic.performHapticFeedback(HapticFeedbackType.LongPress)
              showHistorySheet = !showHistorySheet
            },
            modifier = Modifier.testTag("history_button")
          ) {
            Icon(
              imageVector = Icons.Default.History,
              contentDescription = "السجل",
              tint = if (showHistorySheet) GoldAccent else GrayCool
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "علمية",
              color = if (isScientific) GoldAccent else GrayCool,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Switch(
              checked = isScientific,
              onCheckedChange = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                isScientific = it
              },
              colors = SwitchDefaults.colors(
                checkedThumbColor = ObsidianBG,
                checkedTrackColor = GoldAccent,
                uncheckedThumbColor = GrayCool,
                uncheckedTrackColor = KeySlate
              ),
              modifier = Modifier.scale(0.8f)
            )
          }
        }

        if (showHistorySheet) {
          // Slide open history logs
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .padding(top = 10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "سجل العمليات",
                color = GoldAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "مسح الكل",
                color = ErrorRadical,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                  haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                  previousHistory = emptyList()
                }
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (previousHistory.isEmpty()) {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .weight(1f),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "لا توجد عمليات سابقة",
                  color = GrayCool,
                  fontSize = 12.sp,
                  textAlign = TextAlign.Center
                )
              }
            } else {
              LazyColumn(
                modifier = Modifier
                  .fillMaxSize()
                  .weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                items(previousHistory) { record ->
                  Text(
                    text = record,
                    color = WhiteIce.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                      .fillMaxWidth()
                      .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val part = record.split("=").firstOrNull()?.trim() ?: ""
                        if (part.isNotEmpty()) expression = part
                        showHistorySheet = false
                      }
                      .background(KeyDark, RoundedCornerShape(8.dp))
                      .padding(8.dp),
                    textAlign = TextAlign.End
                  )
                }
              }
            }
          }
        } else {
          // Standard Calculator Screen Output
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
          ) {
            Spacer(modifier = Modifier.weight(1f))
            // Live expression
            Text(
              text = expression.ifEmpty { "0" },
              color = WhiteIce,
              fontSize = if (expression.length > 12) 28.sp else 38.sp,
              fontWeight = FontWeight.Light,
              fontFamily = FontFamily.Monospace,
              textAlign = TextAlign.End,
              overflow = TextOverflow.Ellipsis,
              maxLines = 2,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Calculated Preview / Result
            Text(
              text = resultText,
              color = TealSuccess,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              textAlign = TextAlign.End,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // B. Keypad Grid
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      if (isScientific) {
        // Scientific Keyboard Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ScientificButton(text = "sin", onClick = { appendToExpr("sin(") }, modifier = Modifier.weight(1f))
          ScientificButton(text = "cos", onClick = { appendToExpr("cos(") }, modifier = Modifier.weight(1f))
          ScientificButton(text = "tan", onClick = { appendToExpr("tan(") }, modifier = Modifier.weight(1f))
          ScientificButton(text = "√", onClick = { appendToExpr("√(") }, modifier = Modifier.weight(1f))
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ScientificButton(text = "ln", onClick = { appendToExpr("ln(") }, modifier = Modifier.weight(1f))
          ScientificButton(text = "log", onClick = { appendToExpr("log(") }, modifier = Modifier.weight(1f))
          ScientificButton(text = "^", onClick = { appendToExpr("^") }, modifier = Modifier.weight(1f))
          ScientificButton(text = "(", onClick = { appendToExpr("(") }, modifier = Modifier.weight(1f))
          ScientificButton(text = ")", onClick = { appendToExpr(")") }, modifier = Modifier.weight(1f))
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ScientificButton(text = "π", onClick = { appendToExpr("π") }, modifier = Modifier.weight(1f))
          ScientificButton(text = "e", onClick = { appendToExpr("e") }, modifier = Modifier.weight(1f))
        }
      }

      // Basic Buttons Grid
      val rows = listOf(
        listOf("C", "()", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", "00", ".", "=")
      )

      rows.forEach { row ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          row.forEach { symbol ->
            val weight = if (symbol == "=") 1f else 1f
            val baseColor = when (symbol) {
              "C" -> ErrorRadical
              "=", "÷", "×", "-", "+" -> GoldAccent
              "()", "%" -> GoldDim
              else -> WhiteIce
            }

            val bgStateColor = when (symbol) {
              "C" -> KeyDark
              "=", "÷", "×", "-", "+" -> KeyDark
              else -> KeySlate
            }

            Box(
              modifier = Modifier
                .weight(weight)
                .height(60.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(bgStateColor)
                .testTag("btn_$symbol")
                .clickable {
                  when (symbol) {
                    "C" -> clearAll()
                    "=" -> onCalculate()
                    "()" -> {
                      // Smart parentheses append
                      val openCount = expression.count { it == '(' }
                      val closeCount = expression.count { it == ')' }
                      if (openCount > closeCount && expression.lastOrNull()?.isDigit() == true) {
                        appendToExpr(")")
                      } else {
                        appendToExpr("(")
                      }
                    }
                    else -> appendToExpr(symbol)
                  }
                },
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = symbol,
                color = baseColor,
                fontSize = if (symbol.length > 1) 18.sp else 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          // Backspace button inside first row
          if (row.first() == "C") {
            Box(
              modifier = Modifier
                .weight(1.2f)
                .height(60.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(KeyDark)
                .testTag("btn_backspace")
                .clickable { backspace() },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = "حذف خطوة",
                tint = GoldAccent,
                modifier = Modifier.size(22.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun ScientificButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .height(44.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(KeyDark)
      .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(12.dp))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      color = GoldDim,
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium,
      fontFamily = FontFamily.Monospace
    )
  }
}

// -------------------------------------------------------------
// MODULE 2: PHYSICAL UNIT CONVERTER
// -------------------------------------------------------------
enum class UnitCategory(val title: String, val icon: String) {
  LENGTH("المسافة والمسطحات", "📏"),
  WEIGHT("الوزن والڪتل", "⚖️"),
  VOLUME("الحجم والسوائل", "🥤"),
  TEMPERATURE("درجة الحرارة", "🌡️")
}

@Composable
fun UnitConverterView() {
  val haptic = LocalHapticFeedback.current
  var category by remember { mutableStateOf(UnitCategory.LENGTH) }
  var inputValue by remember { mutableStateOf("1") }

  // Units per category
  val unitsLength = listOf("متر (m)", "كيلومتر (km)", "سنتمتر (cm)", "ميل (mile)", "قدم (ft)", "بوصة (inch)")
  val unitsWeight = listOf("كيلوجرام (kg)", "جرام (g)", "رطل (lbs)", "أونصة (oz)", "طن (ton)")
  val unitsVolume = listOf("لتر (L)", "مليلتر (mL)", "جالون (gal)", "كوب (cup)")
  val unitsTemp = listOf("سيليزيوس (°C)", "فهرنهايت (°F)", "كيلفن (K)")

  val currentUnitsList = when (category) {
    UnitCategory.LENGTH -> unitsLength
    UnitCategory.WEIGHT -> unitsWeight
    UnitCategory.VOLUME -> unitsVolume
    UnitCategory.TEMPERATURE -> unitsTemp
  }

  var fromUnit by remember(category) { mutableStateOf(currentUnitsList.first()) }
  var toUnit by remember(category) { mutableStateOf(currentUnitsList.getOrNull(1) ?: currentUnitsList.first()) }

  // Dynamic convert output
  val convertedResult = remember(inputValue, fromUnit, toUnit, category) {
    val inputValDouble = inputValue.toDoubleOrNull() ?: 0.0
    val res = convertPhysicalUnits(inputValDouble, fromUnit, toUnit, category)
    formatDouble(res)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // A. Selection of Category Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      UnitCategory.values().forEach { cat ->
        val selected = category == cat
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) GoldAccent else CardSlate)
            .clickable {
              haptic.performHapticFeedback(HapticFeedbackType.LongPress)
              category = cat
            }
            .padding(vertical = 10.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = cat.icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = cat.title,
              color = if (selected) ObsidianBG else WhiteIce,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
          }
        }
      }
    }

    // B. Input Value Box
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "إدخال القيمة المراد تحويلها",
          color = GoldAccent,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
          value = inputValue,
          onValueChange = {
            if (it.isEmpty() || it.toDoubleOrNull() != null || it == "-") {
              inputValue = it
            }
          },
          textStyle = androidx.compose.ui.text.TextStyle(
            color = WhiteIce,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End
          ),
          colors = TextFieldDefaults.colors(
            focusedContainerColor = KeyDark,
            unfocusedContainerColor = KeyDark,
            focusedIndicatorColor = GoldAccent,
            unfocusedIndicatorColor = Color.Transparent
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .testTag("unit_input")
        )
      }
    }

    // C. From / To Card Matrix
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // From unit selector column
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "من الوحدة:",
          color = GrayCool,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
        UnitSelectorBox(selectedUnit = fromUnit, units = currentUnitsList) { unit ->
          fromUnit = unit
        }
      }

      // To unit selector column
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "إلى الوحدة:",
          color = GrayCool,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
        UnitSelectorBox(selectedUnit = toUnit, units = currentUnitsList) { unit ->
          toUnit = unit
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // D. Result Dynamic Display Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(
          Brush.horizontalGradient(
            listOf(CardSlate, KeyDark)
          )
        )
        .border(1.dp, GoldAccent.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
        .padding(22.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(text = fromUnit.split(" ").firstOrNull() ?: "", color = GrayCool, fontSize = 14.sp)
          Text(text = "يساوي تقريباً", color = GoldAccent, fontSize = 13.sp)
          Text(text = toUnit.split(" ").firstOrNull() ?: "", color = GrayCool, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "$convertedResult ${toUnit.substringAfter("(").substringBefore(")")}",
          color = TealSuccess,
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitSelectorBox(selectedUnit: String, units: List<String>, onSelect: (String) -> Unit) {
  var expanded by remember { mutableStateOf(false) }

  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = !expanded }
  ) {
    Box(
      modifier = Modifier
        .menuAnchor()
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(CardSlate)
        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
        .padding(14.dp),
      contentAlignment = Alignment.CenterStart
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.ArrowDropDown,
          contentDescription = "توسيع المنسدلة",
          tint = GoldAccent
        )
        Text(
          text = selectedUnit,
          color = WhiteIce,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.End
        )
      }
    }

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      modifier = Modifier.background(CardSlate)
    ) {
      units.forEach { unit ->
        DropdownMenuItem(
          text = {
            Text(
              text = unit,
              color = WhiteIce,
              fontSize = 13.sp,
              modifier = Modifier.fillMaxWidth(),
              textAlign = TextAlign.End
            )
          },
          onClick = {
            onSelect(unit)
            expanded = false
          },
          modifier = Modifier.background(CardSlate)
        )
      }
    }
  }
}

// Physical conversions implementation
fun convertPhysicalUnits(value: Double, from: String, to: String, category: UnitCategory): Double {
  if (from == to) return value

  return when (category) {
    UnitCategory.LENGTH -> {
      // Base unit: Meter
      val meters = when {
        from.contains("كيلومتر") -> value * 1000.0
        from.contains("سنتمتر") -> value * 0.01
        from.contains("ميل") -> value * 1609.34
        from.contains("قدم") -> value * 0.3048
        from.contains("بوصة") -> value * 0.0254
        else -> value
      }
      when {
        to.contains("كيلومتر") -> meters / 1000.0
        to.contains("سنتمتر") -> meters / 0.01
        to.contains("ميل") -> meters / 1609.34
        to.contains("قدم") -> meters / 0.3048
        to.contains("بوصة") -> meters / 0.0254
        else -> meters
      }
    }
    UnitCategory.WEIGHT -> {
      // Base: Kilogram
      val kgs = when {
        from.contains("جرام") -> value * 0.001
        from.contains("رطل") -> value * 0.453592
        from.contains("أونصة") -> value * 0.0283495
        from.contains("طن") -> value * 1000.0
        else -> value
      }
      when {
        to.contains("جرام") -> kgs / 0.001
        to.contains("رطل") -> kgs / 0.453592
        to.contains("أونصة") -> kgs / 0.0283495
        to.contains("طن") -> kgs / 1000.0
        else -> kgs
      }
    }
    UnitCategory.VOLUME -> {
      // Base: Liter
      val liters = when {
        from.contains("مليلتر") -> value * 0.001
        from.contains("جالون") -> value * 3.78541
        from.contains("كوب") -> value * 0.24
        else -> value
      }
      when {
        to.contains("مليلتر") -> liters / 0.001
        to.contains("جالون") -> liters / 3.78541
        to.contains("كوب") -> liters / 0.24
        else -> liters
      }
    }
    UnitCategory.TEMPERATURE -> {
      // Convert to Celsius first
      val celsius = when {
        from.contains("فهرنهايت") -> (value - 32.0) * 5.0 / 9.0
        from.contains("كيلفن") -> value - 273.15
        else -> value
      }
      when {
        to.contains("فهرنهايت") -> (celsius * 9.0 / 5.0) + 32.0
        to.contains("كيلفن") -> celsius + 273.15
        else -> celsius
      }
    }
  }
}

// -------------------------------------------------------------
// MODULE 3: OFFLINE & CUSTOMIZABLE CURRENCY CONVERTER
// -------------------------------------------------------------
@Composable
fun CurrencyConverterView() {
  val haptic = LocalHapticFeedback.current
  var baseAmountStr by remember { mutableStateOf("1") }

  val currencies = listOf(
    "USD - دولار أمريكي",
    "IQD - دينار عراقي",
    "EUR - يورو أوروبي",
    "SAR - ريال سعودي",
    "AED - درهم إماراتي",
    "GBP - جنيه إسترليني",
    "EGP - جنيه مصري"
  )

  var baseCurrency by remember { mutableStateOf(currencies[0]) } // USD default
  var targetCurrency by remember { mutableStateOf(currencies[1]) } // IQD default

  // Base Exchange rates to 1 USD
  var customRateUsdToIqd by remember { mutableStateOf(1310.0) }
  var customRateUsdToEur by remember { mutableStateOf(0.92) }
  var customRateUsdToSar by remember { mutableStateOf(3.75) }
  var customRateUsdToAed by remember { mutableStateOf(3.67) }
  var customRateUsdToGbp by remember { mutableStateOf(0.79) }
  var customRateUsdToEgp by remember { mutableStateOf(48.20) }

  // Exchange rate utility
  fun getUsdValue(currencyName: String): Double {
    val code = currencyName.split(" ").firstOrNull() ?: "USD"
    return when (code) {
      "USD" -> 1.0
      "IQD" -> customRateUsdToIqd
      "EUR" -> customRateUsdToEur
      "SAR" -> customRateUsdToSar
      "AED" -> customRateUsdToAed
      "GBP" -> customRateUsdToGbp
      "EGP" -> customRateUsdToEgp
      else -> 1.0
    }
  }

  // Dynamic calculations
  val calculatedExchange = remember(baseAmountStr, baseCurrency, targetCurrency, customRateUsdToIqd, customRateUsdToEur, customRateUsdToSar, customRateUsdToAed, customRateUsdToGbp, customRateUsdToEgp) {
    val amount = baseAmountStr.toDoubleOrNull() ?: 1.0
    // Convert base currency to USD first
    val rateFromToUsd = 1.0 / getUsdValue(baseCurrency)
    val totalInUsd = amount * rateFromToUsd
    // Convert USD to target currency
    val targetRate = getUsdValue(targetCurrency)
    amount * (targetRate * rateFromToUsd)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // Info Badge
    Card(
      colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(alpha = 0.05f)),
      border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.15f)),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
    ) {
      Row(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = "تنبيه",
          tint = GoldAccent,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = "يمكنك تعديل أسعار الصرف يدوياً من لوحة التحكم بالأسفل لتناسب أسعار السوق الحالية بدقة!",
          color = WhiteIce.copy(alpha = 0.9f),
          fontSize = 11.sp,
          textAlign = TextAlign.End,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // A. Amount Input card
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "مبلغ الصرف المراد تحويله:",
          color = GoldAccent,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
          value = baseAmountStr,
          onValueChange = {
            if (it.isEmpty() || it.toDoubleOrNull() != null) {
              baseAmountStr = it
            }
          },
          textStyle = androidx.compose.ui.text.TextStyle(
            color = WhiteIce,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End
          ),
          colors = TextFieldDefaults.colors(
            focusedContainerColor = KeyDark,
            unfocusedContainerColor = KeyDark,
            focusedIndicatorColor = GoldAccent,
            unfocusedIndicatorColor = Color.Transparent
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .testTag("currency_input")
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // B. Base vs Target Dropdowns
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "العملة الأساسية (من)",
          color = GrayCool,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
        UnitSelectorBox(selectedUnit = baseCurrency, units = currencies) { currency ->
          baseCurrency = currency
        }
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "العملة المستهدفة (إلى)",
          color = GrayCool,
          fontSize = 12.sp,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
        UnitSelectorBox(selectedUnit = targetCurrency, units = currencies) { currency ->
          targetCurrency = currency
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // C. Converted Result Display
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(
          Brush.horizontalGradient(
            listOf(CardSlate, KeyDark)
          )
        )
        .border(1.dp, GoldAccent.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
        .padding(20.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        val baseCode = baseCurrency.split(" ").firstOrNull() ?: ""
        val targetCode = targetCurrency.split(" ").firstOrNull() ?: ""
        Text(
          text = "$baseAmountStr $baseCode يعادل في السوق الموازية:",
          color = GrayCool,
          fontSize = 12.sp,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "${formatDouble(calculatedExchange)} $targetCode",
          color = TealSuccess,
          fontSize = 26.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace,
          textAlign = TextAlign.Center
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // D. Manual Rate Adjusters (Tuning the variables offline!)
    Text(
      text = "لوحة تعديل أسعار الصرف المباشرة (مقابل 1 دولار)",
      color = GoldAccent,
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
    )

    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Adjust Dinar Rate (IQD)
        RateSliderRow(
          title = "الدولار الأمريكي مقابل الدينار العراقي (IQD):",
          value = customRateUsdToIqd,
          range = 1100f..1600f,
          code = "IQD"
        ) { customRateUsdToIqd = it }

        HorizontalDivider(color = Color.White.copy(alpha = 0.03f))

        // Adjust Egyptian Pound (EGP)
        RateSliderRow(
          title = "الدولار الأمريكي مقابل الجنيه المصري (EGP):",
          value = customRateUsdToEgp,
          range = 30f..65f,
          code = "EGP"
        ) { customRateUsdToEgp = it }

        HorizontalDivider(color = Color.White.copy(alpha = 0.03f))

        // Adjust Euro (EUR)
        RateSliderRow(
          title = "الدولار مقابل اليورو الأوروبي (EUR):",
          value = customRateUsdToEur,
          range = 0.8f..1.2f,
          code = "EUR"
        ) { customRateUsdToEur = it }
      }
    }
  }
}

@Composable
fun RateSliderRow(
  title: String,
  value: Double,
  range: ClosedFloatingPointRange<Float>,
  code: String,
  onValChange: (Double) -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${formatDouble(value)} $code",
        color = TealSuccess,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
      )
      Text(
        text = title,
        color = WhiteIce,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
      )
    }
    Slider(
      value = value.toFloat(),
      onValueChange = { onValChange(it.toDouble()) },
      valueRange = range,
      colors = SliderDefaults.colors(
        thumbColor = GoldAccent,
        activeTrackColor = GoldAccent,
        inactiveTrackColor = KeySlate
      ),
      modifier = Modifier.padding(top = 2.dp)
    )
  }
}

// -------------------------------------------------------------
// MODULE 4: BMI HEALTH TRACKER
// -------------------------------------------------------------
@Composable
fun BmiCalculatorView() {
  val haptic = LocalHapticFeedback.current
  var weightKg by remember { mutableStateOf(70f) }
  var heightCm by remember { mutableStateOf(175f) }

  val bmiValue = remember(weightKg, heightCm) {
    val heightMeters = heightCm / 100.0
    if (heightMeters > 0.0) {
      weightKg / (heightMeters * heightMeters)
    } else {
      0.0
    }
  }

  val bmiStatus = remember(bmiValue) {
    when {
      bmiValue < 18.5 -> Pair("نقص في الوزن (Underweight)", ErrorRadical)
      bmiValue < 25.0 -> Pair("وزن مثالي رائع (Normal)", TealSuccess)
      bmiValue < 30.0 -> Pair("زيادة طفيفة بالوزن (Overweight)", GoldAccent)
      else -> Pair("سمنة مفرطة (Obesity)", ErrorRadical)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // 1. Height Picker
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.End
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${heightCm.toInt()} سم",
            color = GoldAccent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "الطول الشخصي (سم):",
            color = WhiteIce,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Slider(
          value = heightCm,
          onValueChange = {
            heightCm = it
          },
          valueRange = 100f..225f,
          colors = SliderDefaults.colors(
            thumbColor = GoldAccent,
            activeTrackColor = GoldAccent,
            inactiveTrackColor = KeySlate
          )
        )
      }
    }

    // 2. Weight Picker
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 14.dp)
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.End
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${String.format("%.1f", weightKg)} كجم",
            color = GoldDim,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "الوزن الحالي (كجم):",
            color = WhiteIce,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Slider(
          value = weightKg,
          onValueChange = {
            weightKg = it
          },
          valueRange = 30f..150f,
          colors = SliderDefaults.colors(
            thumbColor = GoldDim,
            activeTrackColor = GoldDim,
            inactiveTrackColor = KeySlate
          )
        )
      }
    }

    // 3. BMI Result Graphics Gauge
    Card(
      colors = CardDefaults.cardColors(containerColor = KeyDark),
      border = BorderStroke(1.dp, Color.White.copy(alpha = 0.03f)),
      shape = RoundedCornerShape(22.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "مؤشر كتلة الجسم الخاص بك",
          color = GrayCool,
          fontSize = 12.sp,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = String.format("%.1f", bmiValue),
          color = bmiStatus.second,
          fontSize = 44.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = bmiStatus.first,
          color = WhiteIce,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Linear Category Indicator Progress Bar
        val fractionIndicator = remember(bmiValue) {
          ((bmiValue - 15.0) / (35.0 - 15.0)).coerceIn(0.0, 1.0).toFloat()
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(CircleShape)
            .background(KeySlate)
        ) {
          Box(
            modifier = Modifier
              .fillMaxHeight()
              .fillMaxWidth(fractionIndicator)
              .clip(CircleShape)
              .background(
                Brush.horizontalGradient(
                  listOf(TealSuccess, GoldAccent, ErrorRadical)
                )
              )
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Advice Card
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardSlate)
            .padding(12.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.HealthAndSafety,
            contentDescription = "صحة",
            tint = bmiStatus.second,
            modifier = Modifier.size(24.dp)
          )
          Text(
            text = when {
              bmiValue < 18.5 -> "ينصح بزيارة أخصائي تغذية والتركيز على زيادة السعرات الحرارية الصحية اليومية."
              bmiValue < 25.0 -> "ممتاز! حافظ على نظام تغذيتك وممارسة الرياضة للحفاظ على لياقتك الحالية."
              bmiValue < 30.0 -> "ينصح بتقليل السكريات والدهون المشبعة وزيادة الأنشطة البدنية المنتظمة."
              else -> "تنبيه! ينصح بمتابعة حمية غذائية مع أخصائي وتجنب الوجبات السريعة لسلامة قلبك."
            },
            color = WhiteIce.copy(alpha = 0.9f),
            fontSize = 11.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// MODULE 5: BIRTH TIMELINE & AGE CALCULATOR
// -------------------------------------------------------------
@Composable
fun AgeCalculatorView() {
  val context = LocalContext.current
  val haptic = LocalHapticFeedback.current

  var birthYear by remember { mutableStateOf(1998) }
  var birthMonth by remember { mutableStateOf(5) } // 0-indexed Calendar.JUNE
  var birthDay by remember { mutableStateOf(15) }

  // Age calculations
  val ageResults = remember(birthYear, birthMonth, birthDay) {
    val birthCal = Calendar.getInstance().apply {
      set(birthYear, birthMonth, birthDay)
    }
    val today = Calendar.getInstance()

    var years = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
    var months = today.get(Calendar.MONTH) - birthCal.get(Calendar.MONTH)
    var days = today.get(Calendar.DAY_OF_MONTH) - birthCal.get(Calendar.DAY_OF_MONTH)

    if (days < 0) {
      val prevMonth = (today.get(Calendar.MONTH) - 1 + 12) % 12
      val mDays = when (prevMonth) {
        Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
        Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
        Calendar.FEBRUARY -> if (today.get(Calendar.YEAR) % 4 == 0) 29 else 28
        else -> 30
      }
      days += mDays
      months--
    }

    if (months < 0) {
      months += 12
      years--
    }

    // Compute remaining to next birthday
    val nextBday = Calendar.getInstance().apply {
      set(Calendar.MONTH, birthMonth)
      set(Calendar.DAY_OF_MONTH, birthDay)
      if (before(today)) {
        add(Calendar.YEAR, 1)
      }
    }

    val diffMs = nextBday.timeInMillis - today.timeInMillis
    val diffDaysTotal = (diffMs / (1000 * 60 * 60 * 24)).toInt()

    val nextMonths = diffDaysTotal / 30
    val nextDays = diffDaysTotal % 30

    val weekdaysEn = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val weekdaysAr = listOf("الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")
    val bdayDayOfWeekNum = nextBday.get(Calendar.DAY_OF_WEEK) - 1
    val targetWeekday = weekdaysAr.getOrElse(bdayDayOfWeekNum) { "" }

    // Vital Stats Lived Total
    val birthTimeSecs = birthCal.timeInMillis
    val todayTimeSecs = today.timeInMillis
    val totalMsLived = todayTimeSecs - birthTimeSecs
    val totalDays = if (totalMsLived > 0) totalMsLived / (1000 * 60 * 60 * 24) else 0L
    val totalWeeks = totalDays / 7

    AgeReport(
      years = years.coerceAtLeast(0),
      months = months.coerceAtLeast(0),
      days = days.coerceAtLeast(0),
      nextBirthdayMonths = nextMonths.coerceAtLeast(0),
      nextBirthdayDays = nextDays.coerceAtLeast(0),
      nextBirthdayWeekday = targetWeekday,
      totalDaysLived = totalDays,
      totalWeeksLived = totalWeeks
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // DOB Picker trigger button
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = "تاريخ ميلادك المقيد:",
          color = GoldAccent,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "$birthYear - ${birthMonth + 1} - $birthDay",
            color = WhiteIce,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Button(
            onClick = {
              haptic.performHapticFeedback(HapticFeedbackType.LongPress)
              DatePickerDialog(
                context,
                { _, y, m, d ->
                  birthYear = y
                  birthMonth = m
                  birthDay = d
                },
                birthYear, birthMonth, birthDay
              ).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
          ) {
            Text(text = "تحديد التاريخ", color = ObsidianBG, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Results Blocks
    Text(
      text = "العمر الحالي بالتفصيل",
      color = GoldAccent,
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(bottom = 6.dp)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      NumberCard(value = ageResults.years.toString(), label = "سنة", modifier = Modifier.weight(1f))
      NumberCard(value = ageResults.months.toString(), label = "أشهر", modifier = Modifier.weight(1f))
      NumberCard(value = ageResults.days.toString(), label = "يوم", modifier = Modifier.weight(1f))
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Next Birthday Countdown
    Card(
      colors = CardDefaults.cardColors(containerColor = KeyDark),
      border = BorderStroke(1.dp, GoldDim.copy(alpha = 0.15f)),
      shape = RoundedCornerShape(18.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = "تنازلي لذكرى ميلادك القادم 🎉:",
          color = GoldAccent,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "متبقي ${ageResults.nextBirthdayMonths} شهر و ${ageResults.nextBirthdayDays} يوم فقط!",
          color = TealSuccess,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.End,
          modifier = Modifier.fillMaxWidth()
        )
        if (ageResults.nextBirthdayWeekday.isNotEmpty()) {
          Text(
            text = "وسوف يصادف يوم: ${ageResults.nextBirthdayWeekday}",
            color = WhiteIce.copy(alpha = 0.9f),
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Statistics Lived Card
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(18.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "سجل الإشراقات الحياتية التراكمي:",
          color = GoldAccent,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.End
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "${ageResults.totalDaysLived} يوم",
            color = WhiteIce,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "إجمالي الأيام الموشحة:", color = GrayCool, fontSize = 12.sp)
        }
        HorizontalDivider(color = Color.White.copy(alpha = 0.03f))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "${ageResults.totalWeeksLived} أسبوع",
            color = WhiteIce,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "إجمالي الأسابيع التي عشتها:", color = GrayCool, fontSize = 12.sp)
        }
      }
    }
  }
}

data class AgeReport(
  val years: Int,
  val months: Int,
  val days: Int,
  val nextBirthdayMonths: Int,
  val nextBirthdayDays: Int,
  val nextBirthdayWeekday: String,
  val totalDaysLived: Long,
  val totalWeeksLived: Long
)

@Composable
fun NumberCard(value: String, label: String, modifier: Modifier = Modifier) {
  Card(
    colors = CardDefaults.cardColors(containerColor = CardSlate),
    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.03f)),
    shape = RoundedCornerShape(16.dp),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = value,
        color = GoldAccent,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        fontFamily = FontFamily.Monospace
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = label,
        color = GrayCool,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

// -------------------------------------------------------------
// MODULE 6: DISCOUNT & VAT CALCULATION
// -------------------------------------------------------------
@Composable
fun DiscountCalculatorView() {
  val haptic = LocalHapticFeedback.current
  var originalPriceStr by remember { mutableStateOf("100000") }
  var discountPercent by remember { mutableStateOf(15f) }
  var taxPercent by remember { mutableStateOf(5f) }

  val calculations = remember(originalPriceStr, discountPercent, taxPercent) {
    val orig = originalPriceStr.toDoubleOrNull() ?: 0.0
    val discAmt = orig * (discountPercent / 100.0)
    val afterDisc = orig - discAmt
    val taxAmt = afterDisc * (taxPercent / 100.0)
    val finalVal = afterDisc + taxAmt
    val savedAmount = discAmt - taxAmt

    DiscountReport(
      original = orig,
      discountAmt = discAmt,
      taxAmt = taxAmt,
      finalPaid = finalVal,
      netSaved = savedAmount
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // 1. Original Price Input card
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "السعر الأصلي للسلعة:",
          color = GoldAccent,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
          value = originalPriceStr,
          onValueChange = {
            if (it.isEmpty() || it.toDoubleOrNull() != null) {
              originalPriceStr = it
            }
          },
          textStyle = androidx.compose.ui.text.TextStyle(
            color = WhiteIce,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End
          ),
          colors = TextFieldDefaults.colors(
            focusedContainerColor = KeyDark,
            unfocusedContainerColor = KeyDark,
            focusedIndicatorColor = GoldAccent,
            unfocusedIndicatorColor = Color.Transparent
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .testTag("discount_price_input")
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 2. Custom Percentage Sliders
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Discount Slider
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${discountPercent.toInt()}%",
              color = TealSuccess,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(text = "نسبة الخصم (%):", color = WhiteIce, fontSize = 12.sp)
          }
          Slider(
            value = discountPercent,
            onValueChange = { discountPercent = it },
            valueRange = 0f..95f,
            colors = SliderDefaults.colors(
              thumbColor = GoldAccent,
              activeTrackColor = GoldAccent,
              inactiveTrackColor = KeySlate
            )
          )
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.03f))

        // Tax Slider
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${taxPercent.toInt()}%",
              color = ErrorRadical,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(text = "نسبة الضريبة والخدمة (%):", color = WhiteIce, fontSize = 12.sp)
          }
          Slider(
            value = taxPercent,
            onValueChange = { taxPercent = it },
            valueRange = 0f..25f,
            colors = SliderDefaults.colors(
              thumbColor = GoldDim,
              activeTrackColor = GoldDim,
              inactiveTrackColor = KeySlate
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 3. Receipt Invoice Output
    Text(
      text = "الفاتورة التفصيلية للسلعة",
      color = GoldAccent,
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(bottom = 6.dp)
    )

    Card(
      colors = CardDefaults.cardColors(containerColor = KeyDark),
      border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.12f)),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = formatDouble(calculations.original),
            color = WhiteIce,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "السعر الأصلي:", color = GrayCool, fontSize = 12.sp)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "- ${formatDouble(calculations.discountAmt)}",
            color = TealSuccess,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "قيمة التخفيض الموفرة:", color = GrayCool, fontSize = 12.sp)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "+ ${formatDouble(calculations.taxAmt)}",
            color = ErrorRadical,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "الضريبة المضافة:", color = GrayCool, fontSize = 12.sp)
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = formatDouble(calculations.finalPaid),
            color = GoldAccent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "المبلغ المطلوب سداده:", color = WhiteIce, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Savvy text
        if (calculations.netSaved > 0) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(TealSuccess.copy(alpha = 0.1f))
              .padding(8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "لقد وفرت مبلع إجمالي ${formatDouble(calculations.netSaved)} في هذه العملية 🎉!",
              color = TealSuccess,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}

data class DiscountReport(
  val original: Double,
  val discountAmt: Double,
  val taxAmt: Double,
  val finalPaid: Double,
  val netSaved: Double
)

// -------------------------------------------------------------
// MODULE 7: LOAN & CREDIT EMI CALCULATOR
// -------------------------------------------------------------
@Composable
fun LoanCalculatorView() {
  val haptic = LocalHapticFeedback.current
  var loanAmountStr by remember { mutableStateOf("5000000") }
  var annualInterestRate by remember { mutableStateOf(7.5f) }
  var loanTenureMonths by remember { mutableStateOf(24f) }

  val emiCalculations = remember(loanAmountStr, annualInterestRate, loanTenureMonths) {
    val principal = loanAmountStr.toDoubleOrNull() ?: 0.0
    val monthlyRate = (annualInterestRate / 12.0) / 100.0
    val months = loanTenureMonths.toInt()

    val emi = if (principal > 0.0 && monthlyRate > 0.0 && months > 0) {
      val divisor = Math.pow(1.0 + monthlyRate, months.toDouble()) - 1.0
      (principal * monthlyRate * Math.pow(1.0 + monthlyRate, months.toDouble())) / divisor
    } else if (principal > 0.0 && months > 0) {
      principal / months
    } else {
      0.0
    }

    val totalAmountOfPayment = emi * months
    val totalInterestOfPayment = totalAmountOfPayment - principal

    LoanEmiReport(
      monthlyEmi = emi,
      totalInterest = totalInterestOfPayment.coerceAtLeast(0.0),
      totalPayable = totalAmountOfPayment.coerceAtLeast(0.0)
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // 1. Principal Loan Amount Input card
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "مبلغ القرض المطلوب:",
          color = GoldAccent,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
          value = loanAmountStr,
          onValueChange = {
            if (it.isEmpty() || it.toDoubleOrNull() != null) {
              loanAmountStr = it
            }
          },
          textStyle = androidx.compose.ui.text.TextStyle(
            color = WhiteIce,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End
          ),
          colors = TextFieldDefaults.colors(
            focusedContainerColor = KeyDark,
            unfocusedContainerColor = KeyDark,
            focusedIndicatorColor = GoldAccent,
            unfocusedIndicatorColor = Color.Transparent
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .testTag("loan_input")
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 2. Sliders
    Card(
      colors = CardDefaults.cardColors(containerColor = CardSlate),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Interest rate
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = String.format("%.1f%%", annualInterestRate),
              color = GoldAccent,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(text = "معدل الفائدة السنوي (%):", color = WhiteIce, fontSize = 12.sp)
          }
          Slider(
            value = annualInterestRate,
            onValueChange = { annualInterestRate = it },
            valueRange = 1f..25f,
            colors = SliderDefaults.colors(
              thumbColor = GoldAccent,
              activeTrackColor = GoldAccent,
              inactiveTrackColor = KeySlate
            )
          )
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.03f))

        // Duration terms
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${loanTenureMonths.toInt()} شهر",
              color = GoldDim,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(text = "مدة سداد القسط بالشهور:", color = WhiteIce, fontSize = 12.sp)
          }
          Slider(
            value = loanTenureMonths,
            onValueChange = { loanTenureMonths = it },
            valueRange = 3f..120f,
            colors = SliderDefaults.colors(
              thumbColor = GoldDim,
              activeTrackColor = GoldDim,
              inactiveTrackColor = KeySlate
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 3. Results Overview Box
    Card(
      colors = CardDefaults.cardColors(containerColor = KeyDark),
      border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.12f)),
      shape = RoundedCornerShape(22.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "القسط الشهري المطلوب (Equated Monthly Installment)",
          color = GrayCool,
          fontSize = 11.sp,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = formatDouble(emiCalculations.monthlyEmi),
          color = GoldAccent,
          fontSize = 28.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = formatDouble(emiCalculations.totalInterest),
            color = ErrorRadical,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "إجمالي مبلغ الفوائد الإضافية:", color = GrayCool, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = formatDouble(emiCalculations.totalPayable),
            color = TealSuccess,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Text(text = "المبلغ الإجمالي المطلق سداده:", color = GrayCool, fontSize = 11.sp)
        }
      }
    }
  }
}

data class LoanEmiReport(
  val monthlyEmi: Double,
  val totalInterest: Double,
  val totalPayable: Double
)

// -------------------------------------------------------------
// HELPER MATH & PARSER SUB-ROUTINES (Recursive Descent Parser)
// -------------------------------------------------------------
fun formatDouble(value: Double): String {
  if (value.isNaN() || value.isInfinite()) return "خطأ حسابي"
  // If is integer mathematically
  return if (value % 1.0 == 0.0) {
    if (value > 1000000.0) {
      String.format(Locale.US, "%,.0f", value)
    } else {
      value.toLong().toString()
    }
  } else {
    // Show up to 4 decimals cleanly
    val formatted = String.format(Locale.US, "%.4f", value)
    // Erase trailing zeros
    var clean = formatted
    while (clean.endsWith("0")) {
      clean = clean.dropLast(1)
    }
    if (clean.endsWith(".")) {
      clean = clean.dropLast(1)
    }
    clean
  }
}

fun evaluateExpression(str: String): Double {
  // Sanitize and replace operations
  var cleanExpr = str
    .replace("×", "*")
    .replace("÷", "/")
    .replace("√", "sqrt")
    .replace("π", Math.PI.toString())
    .replace("e", Math.E.toString())

  // Parse scientific parenthesis function multiplications: e.g. "5(2)" -> "5*(2)"
  val regexMultiplyOutsideParen = "(\\d)(\\()".toRegex()
  cleanExpr = cleanExpr.replace(regexMultiplyOutsideParen, "$1*$2")

  // Handle percentages inline: convert x% to x*0.01
  cleanExpr = cleanExpr.replace("%", "*0.01")

  return object {
    var pos = -1
    var ch = 0

    fun nextChar() {
      ch = if (++pos < cleanExpr.length) cleanExpr[pos].code else -1
    }

    fun eat(charToEat: Int): Boolean {
      while (ch == ' '.code) nextChar()
      if (ch == charToEat) {
        nextChar()
        return true
      }
      return false
    }

    fun parse(): Double {
      nextChar()
      val x = parseExpression()
      if (pos < cleanExpr.length) throw RuntimeException("Syntax Error at char: " + ch.toChar())
      return x
    }

    fun parseExpression(): Double {
      var x = parseTerm()
      while (true) {
        if (eat('+'.code)) x += parseTerm() // addition
        else if (eat('-'.code)) x -= parseTerm() // subtraction
        else return x
      }
    }

    fun parseTerm(): Double {
      var x = parseFactor()
      while (true) {
        if (eat('*'.code)) x *= parseFactor() // multiplication
        else if (eat('/'.code)) {
          val divisor = parseFactor()
          if (divisor == 0.0) throw ArithmeticException("Divide by zero")
          x /= divisor // division
        } else return x
      }
    }

    fun parseFactor(): Double {
      if (eat('+'.code)) return parseFactor() // unary plus
      if (eat('-'.code)) return -parseFactor() // unary minus

      var x: Double
      val startPos = pos
      if (eat('('.code)) { // parentheses
        x = parseExpression()
        eat(')'.code)
      } else if ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) { // numbers
        while ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) nextChar()
        x = cleanExpr.substring(startPos, pos).toDouble()
      } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
        while (ch >= 'a'.code && ch <= 'z'.code) nextChar()
        val func = cleanExpr.substring(startPos, pos)
        if (eat('('.code)) {
          x = parseExpression()
          eat(')'.code)
        } else {
          x = parseFactor() // single factor argument
        }
        x = when (func) {
          "sin" -> Math.sin(Math.toRadians(x))
          "cos" -> Math.cos(Math.toRadians(x))
          "tan" -> Math.tan(Math.toRadians(x))
          "ln" -> Math.log(x)
          "log" -> Math.log10(x)
          "sqrt" -> {
            if (x < 0.0) throw ArithmeticException("Square root of negative")
            Math.sqrt(x)
          }
          else -> throw RuntimeException("Unknown function: $func")
        }
      } else {
        throw RuntimeException("Unexpected: " + ch.toChar())
      }

      if (eat('^'.code)) x = Math.pow(x, parseFactor()) // exponentiation

      return x
    }
  }.parse()
}

// -------------------------------------------------------------
// EXTRA COMPONENT UTILITIES
// -------------------------------------------------------------
fun Modifier.scale(scale: Float): Modifier = this.then(
  graphicsLayer(scaleX = scale, scaleY = scale)
)


// -------------------------------------------------------------
// MODULE 8: INTEGRATED SCHOOL EXEMPTION SYSTEM
// -------------------------------------------------------------
enum class IraqiSchoolStage(val displayName: String, val subjects: List<String>) {
  MIDDLE("الأول والثاني متوسط", listOf(
    "التربية الإسلامية",
    "اللغة العربية",
    "اللغة الإنجليزية",
    "الرياضيات",
    "الاجتماعيات",
    "الكيمياء",
    "الفيزياء",
    "الأحياء",
    "الأخلاقية"
  )),
  FOURTH_SCIENTIFIC("الرابع العلمي", listOf(
    "التربية الإسلامية",
    "اللغة العربية",
    "اللغة الإنجليزية",
    "الرياضيات",
    "الكيمياء",
    "الفيزياء",
    "الأحياء",
    "الحاسوب",
    "جرائم حزب البعث"
  )),
  FIFTH_SCIENTIFIC("الخامس العلمي", listOf(
    "التربية الإسلامية",
    "اللغة العربية",
    "اللغة الإنجليزية",
    "الرياضيات",
    "الكيمياء",
    "الفيزياء",
    "الأحياء",
    "الحاسوب"
  ))
}

data class SubjectResult(
  val name: String,
  val originalScore: Double,
  val roundedScore: Double,
  val isExemptedIndividually: Boolean
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SchoolExemptionView() {
  val haptic = LocalHapticFeedback.current
  var selectedStage by remember { mutableStateOf(IraqiSchoolStage.MIDDLE) }

  // We store the subjects score in a mutable state table
  var subjectScores by remember(selectedStage) {
    mutableStateOf(selectedStage.subjects.associateWith { "" })
  }

  // Error and calculation results states
  var errorText by remember { mutableStateOf<String?>(null) }
  var calculatedAverage by remember { mutableStateOf<Double?>(null) }
  var isGeneralExemptionAchieved by remember { mutableStateOf<Boolean?>(null) }
  var subjectsResultList by remember { mutableStateOf<List<SubjectResult>>(emptyList()) }
  var calculationPerformed by remember { mutableStateOf(false) }

  // Helper calculation function
  fun performCalculation() {
    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    errorText = null
    
    // Validate inputs
    val scoresParsed = mutableMapOf<String, Double>()
    for (subject in selectedStage.subjects) {
      val scoreStr = subjectScores[subject]?.trim() ?: ""
      if (scoreStr.isEmpty()) {
        errorText = "الرجاء إدخال درجة مادة ($subject)"
        calculationPerformed = false
        return
      }
      val parsed = scoreStr.toDoubleOrNull()
      if (parsed == null || parsed < 0.0 || parsed > 100.0) {
        errorText = "يرجى كتابة درجة صحيحة مابين 0 و 100 لمادة ($subject)"
        calculationPerformed = false
        return
      }
      scoresParsed[subject] = parsed
    }

    // Apply rounding rules to each subject
    // Rules:
    // If grade >= 74.6 and < 75 -> Round up/raise automatically to 75.
    // If grade >= 89.6 and < 90 -> Round up/raise automatically to 90.
    val roundedScores = scoresParsed.mapValues { (_, score) ->
      if (score >= 74.6 && score < 75.0) 75.0
      else if (score >= 89.6 && score < 90.0) 90.0
      else score
    }

    // Average: Sum of rounded scores divided by total number of subjects
    val totalSum = roundedScores.values.sum()
    val average = totalSum / selectedStage.subjects.size
    calculatedAverage = average

    // general exemption conditions:
    // 1. Average >= 85
    // 2. No rounded score less than 75
    val hasNoUnder75 = roundedScores.values.all { it >= 75.0 }
    val meetsAvg = average >= 85.0
    val isGeneral = meetsAvg && hasNoUnder75
    isGeneralExemptionAchieved = isGeneral

    // Individual exemption check: if rounded score >= 90
    subjectsResultList = selectedStage.subjects.map { name ->
      val orig = scoresParsed[name] ?: 0.0
      val rounded = roundedScores[name] ?: 0.0
      SubjectResult(
        name = name,
        originalScore = orig,
        roundedScore = rounded,
        isExemptedIndividually = rounded >= 90.0
      )
    }

    calculationPerformed = true
  }

  // Pre-fill helpers
  fun preFillSample(type: String) {
    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    calculationPerformed = false
    val samples = when (type) {
      "excellent" -> {
        // High scores causing general exemption
        mapOf(
          "التربية الإسلامية" to "95",
          "اللغة العربية" to "88",
          "اللغة الإنجليزية" to "84.6", // round to 85
          "الرياضيات" to "90",
          "الاجتماعيات" to "74.8", // round to 75
          "الكيمياء" to "92",
          "الفيزياء" to "89.6", // round to 90
          "الأحياء" to "87",
          "الأخلاقية" to "96",
          "الحاسوب" to "95",
          "جرائم حزب البعث" to "98"
        )
      }
      else -> {
        // mixed grades
        mapOf(
          "التربية الإسلامية" to "92", // exempt (>= 90)
          "اللغة العربية" to "82", // exam
          "اللغة الإنجليزية" to "68", // exam
          "الرياضيات" to "89.7", // round to 90 -> exempt
          "الاجتماعيات" to "73", // exam
          "الكيمياء" to "74.7", // round to 75 -> exam
          "الفيزياء" to "91", // exempt
          "الأحياء" to "85", // exam
          "الأخلاقية" to "95", // exam
          "الحاسوب" to "90", // exempt
          "جرائم حزب البعث" to "88" // exam
        )
      }
    }
    subjectScores = selectedStage.subjects.associateWith { name ->
      samples[name] ?: "80"
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Selector Dropdown Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = CardSlate),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.End
        ) {
          Text(
            text = "اختر المرحلة والصف الدراسي:",
            color = GoldAccent,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
          )

          var dropdownExpanded by remember { mutableStateOf(false) }

          ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
          ) {
            Box(
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(KeyDark)
                .border(1.dp, GoldAccent.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                .clickable { dropdownExpanded = !dropdownExpanded }
                .padding(16.dp),
              contentAlignment = Alignment.CenterEnd
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.ArrowDropDown,
                  contentDescription = "قائمة",
                  tint = GoldAccent
                )
                Text(
                  text = selectedStage.displayName,
                  color = WhiteIce,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            ExposedDropdownMenu(
              expanded = dropdownExpanded,
              onDismissRequest = { dropdownExpanded = false },
              modifier = Modifier.background(CardSlate)
            ) {
              IraqiSchoolStage.values().forEach { stage ->
                DropdownMenuItem(
                  text = {
                    Text(
                      text = stage.displayName,
                      color = WhiteIce,
                      fontSize = 14.sp,
                      modifier = Modifier.fillMaxWidth(),
                      textAlign = TextAlign.End
                    )
                  },
                  onClick = {
                    selectedStage = stage
                    calculationPerformed = false
                    dropdownExpanded = false
                  },
                  modifier = Modifier.background(CardSlate)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Subject number info label
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "عدد المواد: ${selectedStage.subjects.size} مواد مدرجة",
              color = TealSuccess,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "info",
              tint = TealSuccess,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }

    // Quick Action Tools: Pre-fill Buttons
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = { preFillSample("mixed") },
          colors = ButtonDefaults.buttonColors(containerColor = KeySlate),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text("تعبئة درجات متفاوتة", color = WhiteIce, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Button(
          onClick = { preFillSample("excellent") },
          colors = ButtonDefaults.buttonColors(containerColor = KeyDark),
          border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.3f)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text("تعبئة درجات متفوقة", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // 2. Subjects Inputs List
    items(selectedStage.subjects) { subject ->
      val scoreStr = subjectScores[subject] ?: ""

      // Live round up calculation
      val parsedNum = scoreStr.toDoubleOrNull()
      val roundedPreview = if (parsedNum != null) {
        if (parsedNum >= 74.6 && parsedNum < 75.0) 75.0
        else if (parsedNum >= 89.6 && parsedNum < 90.0) 90.0
        else null
      } else null

      Card(
        colors = CardDefaults.cardColors(containerColor = CardSlate),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          // Dynamic Rounding Badge
          Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
          ) {
            if (roundedPreview != null) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(TealSuccess.copy(alpha = 0.15f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "تُرفع إلى: ${roundedPreview.toInt()}",
                  color = TealSuccess,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            } else {
              Text(
                text = "سعي السنوية",
                color = GrayCool,
                fontSize = 11.sp
              )
            }
          }

          // Input Textfield
          TextField(
            value = scoreStr,
            onValueChange = { input ->
              if (input.isEmpty() || input.toDoubleOrNull() != null) {
                subjectScores = subjectScores.toMutableMap().apply {
                  put(subject, input)
                }
                calculationPerformed = false
              }
            },
            placeholder = {
              Text(
                "أدخل الدرجة",
                color = GrayCool.copy(alpha = 0.5f),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
              )
            },
            textStyle = androidx.compose.ui.text.TextStyle(
              color = WhiteIce,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              textAlign = TextAlign.End
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
              focusedContainerColor = KeyDark,
              unfocusedContainerColor = KeyDark,
              focusedIndicatorColor = GoldAccent,
              unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
              .width(95.dp)
              .height(52.dp)
              .clip(RoundedCornerShape(10.dp))
              .testTag("score_input_$subject")
          )

          Spacer(modifier = Modifier.width(12.dp))

          // Subject Label
          Text(
            text = subject,
            color = WhiteIce,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .widthIn(min = 90.dp)
              .wrapContentWidth(Alignment.End),
            textAlign = TextAlign.End
          )
        }
      }
    }

    // Error Message
    errorText?.let { err ->
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = ErrorRadical.copy(alpha = 0.15f)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = err,
            color = ErrorRadical,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp)
          )
        }
      }
    }

    // 3. Huge Calculate Button
    item {
      Button(
        onClick = { performCalculation() },
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .testTag("btn_calculate_exemption"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(GoldAccent, GoldDim))),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = "Stars",
              tint = ObsidianBG,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "احسب النتيجة ومعالجة الإعفاء",
              color = ObsidianBG,
              fontSize = 15.sp,
              fontWeight = FontWeight.Black
            )
          }
        }
      }
    }

    // 4. Detailed Results Panel
    if (calculationPerformed) {
      item {
        val avgVal = calculatedAverage ?: 0.0
        val isGen = isGeneralExemptionAchieved == true

        Card(
          colors = CardDefaults.cardColors(containerColor = KeyDark),
          border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.15f)),
          shape = RoundedCornerShape(22.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Header
            Text(
              text = "بطاقة نتيجة الإعفاء النهائي",
              color = GoldAccent,
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))

            // State Title Block
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                  if (isGen) TealSuccess.copy(alpha = 0.15f) else KeySlate
                )
                .border(
                  width = 1.dp,
                  color = if (isGen) TealSuccess else Color.White.copy(alpha = 0.05f),
                  shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "المعدل العام للسعي السنوي:",
                  color = GrayCool,
                  fontSize = 12.sp,
                  textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "${String.format("%.2f", avgVal)}%",
                  color = if (avgVal >= 85.0) GoldAccent else WhiteIce,
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Black,
                  fontFamily = FontFamily.Monospace,
                  textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = if (isGen) "مبروك! إعفاء عام من جميع المواد 🎉" else "شبه معفى أو مشمول بالإعفاءات الفردية",
                  color = if (isGen) TealSuccess else GoldDim,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  textAlign = TextAlign.Center
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Subject-by-subject status breakdown list
            Text(
              text = "حالة المواد بالتفصيل مادة تلو الأخرى:",
              color = WhiteIce,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
              textAlign = TextAlign.End
            )

            Column(
              verticalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              subjectsResultList.forEach { result ->
                val isExempt = isGen || result.isExemptedIndividually

                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardSlate)
                    .padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  // Left side status tag
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(
                        if (isExempt) {
                          TealSuccess.copy(alpha = 0.12f)
                        } else {
                          ErrorRadical.copy(alpha = 0.12f)
                        }
                      )
                      .border(
                        1.dp,
                        if (isExempt) TealSuccess.copy(alpha = 0.3f) else ErrorRadical.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                      )
                      .padding(horizontal = 10.dp, vertical = 6.dp)
                  ) {
                    Text(
                      text = if (isGen) "معفى (إعفاء عام)" else if (result.isExemptedIndividually) "معفى إعفاء فردي" else "مشمول بالامتحان النهائي",
                      color = if (isExempt) TealSuccess else ErrorRadical,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }

                  // Center grade info
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(
                      text = "(${result.roundedScore.toInt()})",
                      color = GoldAccent,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      fontFamily = FontFamily.Monospace
                    )
                    Text(
                      text = "${result.originalScore}",
                      color = WhiteIce,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Medium,
                      fontFamily = FontFamily.Monospace
                    )
                  }

                  // Right subject name
                  Text(
                    text = result.name,
                    color = WhiteIce,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }

    // FAQ System info card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = CardSlate.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.End
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "تعليمات حساب الإعفاء المدرسي العراقي",
              color = GoldAccent,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
            Icon(
              imageVector = Icons.Default.MenuBook,
              contentDescription = "تعليمات",
              tint = GoldAccent,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "١. جبر الكسور: يتم جبر درجات السعي السنوي الفردية تلقائياً للمواد (فالدرجة المترددة من 74.6 إلى 74.9 ترفع لـ 75، ومن 89.6 إلى 89.9 ترفع لـ 90) لتسهيل استحقاق الإعفاء.",
            color = WhiteIce.copy(alpha = 0.8f),
            fontSize = 11.sp,
            textAlign = TextAlign.End,
            lineHeight = 16.sp,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "٢. الإعفاء العام: تشترط وزارة التربية الحصول على معدل عام لا يقل عن 85% مع شرط عدم انخفاض أي مادة عن درجة 75% بعد التقريب.",
            color = WhiteIce.copy(alpha = 0.8f),
            fontSize = 11.sp,
            textAlign = TextAlign.End,
            lineHeight = 16.sp,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "٣. الإعفاء الفردي: يتم إعفاء الطالب فردياً في أي مادة تصل درجتها السنوية إلى 90% أو أكثر بشكل قائم بذاته ومستقل.",
            color = WhiteIce.copy(alpha = 0.8f),
            fontSize = 11.sp,
            textAlign = TextAlign.End,
            lineHeight = 16.sp,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}

