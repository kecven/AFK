# AFK

Universal clicker based on GraalVM and JavaScript

# Methods

## Key

    void press(int key)
    void press(String key)
    void down(int key)
    void up(int key)
    void setClipboard(String text)
    String getClipboard()
    void setHotKey(String hotKey, Predicate<Object> func)
    void setSyncHotKey(String hotKey, Predicate<Object> func)
    void stopHotKey()
    int getKey(String code)

## Mouse

    void click()
    void click(int x, int y)
    void click(int button)
    void click(int button, int x, int y)
    void dblClick()
    void dblClick(int x, int y)
    void dblClick(int button)
    void dblClick(int button, int x, int y)
    void down()
    void down(int x, int y)
    void down(int button)
    void down(int button, int x, int y)
    void up()
    void up(int x, int y)

    // up mouse button
    // @param button 0 - left, 1 - middle, 2 - right
    void up(int button)
    void up(int button, int x, int y)
    void move(int x, int y)
    void move(int x, int y, int time)

    // get location of mouse
    Point getLoc()

## System


    /**
     * print text to console
     * @param msg
     */
    void print(String msg)

    /**
     * println text to console
     */
    void println()

    /**
     * println text to console
     * @param msg
     */
    void println(String msg)

    /**
     * print text to console
     * @param msg
     */
    void printError(String msg)

    /**
     * println text to console
     * @param msg
     */
    void printlnError(String msg)

    /**
     * sleep
     * @param time
     */
    void sleep(long time)

    /**
     * получить текущее количество милисикунд
     */
    long time()

    /**
     * Запуск функции в отдельнос потоке
     */
    void run(String scriptName, Predicate<Object> func)

    /**
     * Запуск функции в отдельнос потоке
     */
    void run(Predicate<Object> func)

    /**
     * Выполнение команды
     */
    Process exec(String command)

    /**
     * получить цвет пикселя
     */
    Color getPixelColor(int x, int y)

    /**
     * Тип системы(Linux / Windows)..
     */
    String getOs()

    /**
     * Информация о системе
     */
    String sysInfo(String info)

    /**
     * Вывести информационное сообщение
     * @param text
     * @param time
     */
    void alert(String text, int time)

    /**
     * Получить разрешение экрана
     * @return
     */
    Dimension getScreenSize()

    /**
     * Послать сообщение другому скрипту
     * @return
     */
    void sendSignal(String script, String msg)

    /**
     * Получить список всех сообщений для данного скрипта
     * @return
     */
    java.util.List getSignal(String script)

