# Maven Wrapper for PowerShell
$mavenBin = "C:\Users\Lenovo\tools\apache-maven-3.9.16\bin"
if (Test-Path "$mavenBin\mvn.cmd") {
    & "$mavenBin\mvn.cmd" $args
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn $args
} else {
    Write-Error "Maven not found in PATH or at $mavenBin"
    exit 1
}
